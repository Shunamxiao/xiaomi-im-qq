'use strict';
// Frida agent injected into com.tencent.mobileqq (main process).
// RPC: status() / publish(guildId, channelId, text, posterId)
//
// Proven chain (2026-11-15, QQ 9.3.70 arm64):
//   IGProSession(runtimeService).getFeedService().publishFeed(req, cb) on MAIN thread,
//   two-phase callback: ack(retCode=0,ok=false) then final rsp with feed.idd.
//   req.jsonFeed is authoritative; channelInfo.sign carries ids; httpReqInfo carries pskey.

var TAG = '[agent] ';
var g = { fGuild: null, feedService: null, appRuntime: null, NI: null, ready: false };

function useAny(name) {
  try { return g.fGuild.use(name); } catch (e) {}
  try { return Java.use(name); } catch (e) {}
  var cls = null;
  Java.enumerateClassLoadersSync().forEach(function (ld) {
    if (cls) return;
    try { cls = Java.ClassFactory.get(ld).use(name); } catch (e) {}
  });
  if (!cls) throw new Error('class not found: ' + name);
  return cls;
}

function init() {
  if (g.ready) return;
  var picked = null;
  Java.enumerateClassLoadersSync().forEach(function (ld) {
    if (picked) return;
    try {
      var f = Java.ClassFactory.get(ld);
      var dm = f.use('com.tencent.mobileqq.guild.util.dm');
      var rt = dm.N();
      if (rt !== null) picked = { f: f, dm: dm, rt: rt };
    } catch (e) {}
  });
  if (!picked) throw new Error('no classloader yields a live AppRuntime');
  var found = picked.f;
  var IGProSession = found.use('com.tencent.mobileqq.qqguildsdk.api.IGProSession');
  var rt = picked.rt;
  var session = Java.cast(rt.getRuntimeService(IGProSession.class, ''), IGProSession);
  if (!session.isInit()) throw new Error('GPro session not init');
  g.fGuild = found;
  g.appRuntime = rt;
  g.feedService = session.getFeedService();
  g.NI = {
    Req: useAny('com.tencent.qqnt.kernelgpro.nativeinterface.GProStPublishFeedReq'),
    Sign: useAny('com.tencent.qqnt.kernelgpro.nativeinterface.GProStChannelSign'),
    ChanInfo: useAny('com.tencent.qqnt.kernelgpro.nativeinterface.GProStChannelInfo'),
    Feed: useAny('com.tencent.qqnt.kernelgpro.nativeinterface.GProStFeed'),
    HttpInfo: useAny('com.tencent.qqnt.kernelgpro.nativeinterface.GProHttpReqInfo'),
    Ext: useAny('com.tencent.qqnt.kernelgpro.nativeinterface.GProStCommonExt'),
    Entry: useAny('com.tencent.qqnt.kernelgpro.nativeinterface.GProFeedEntry'),
    PubCb: useAny('com.tencent.qqnt.kernelgpro.nativeinterface.IGPropublishFeedCallback')
  };
  g.ready = true;
}

// promise-based pskey fetch (non-blocking)
function getPskey(domain, timeoutMs) {
  var IPskeyManager = g.fGuild.use('com.tencent.mobileqq.pskey.api.IPskeyManager');
  var mgr = Java.cast(g.appRuntime.getRuntimeService(IPskeyManager.class, ''), IPskeyManager);
  var holder = g.pskeyCbHolder;
  holder.domain = domain;
  holder.v = null;
  mgr.getPskey([domain], g.pskeyCbInstance);
  return new Promise(function (resolve) {
    var waited = 0;
    var t = setInterval(function () {
      waited += 200;
      if (holder.v !== null || waited > (timeoutMs || 8000)) {
        clearInterval(t);
        resolve(holder.v || '');
      }
    }, 200);
  });
}

function buildReq(guildIdS, channelIdS, text, posterIdS, pskey) {
  var NI = g.NI;
  var JLong = Java.use('java.lang.Long');
  var CLIENT_KEY = 'NodeSvc_' + Date.now() + '_' + Math.random().toString(16).slice(2, 10);
  var sign = NI.Sign.$new();
  sign.guildId.value = JLong.parseLong(guildIdS);
  sign.channelId.value = JLong.parseLong(channelIdS);
  var chanInfo = NI.ChanInfo.$new();
  chanInfo.sign.value = sign;
  chanInfo.isSquare.value = true;
  var feed = NI.Feed.$new();
  feed.channelInfo.value = chanInfo;
  feed.clientTaskId.value = CLIENT_KEY;
  var req = NI.Req.$new();
  req.feed.value = feed;
  req.jsonFeed.value = JSON.stringify({
    feed_type: 1,
    title: {},
    poster: { id: posterIdS },
    channelInfo: { sign: { guild_id: guildIdS, channel_id: channelIdS }, is_square: true },
    tagInfos: [], recommend_channels: [], images: [], videos: [],
    client_task_id: CLIENT_KEY,
    media_lock_count: 0,
    content_with_style: { paragraphs: [{ elems: [{ text: { text_content: { text: text } }, type: 1 }] }] },
    advanced_settings: { not_allow_download: false, show_watermark: false },
    files: []
  });
  var httpInfo = NI.HttpInfo.$new();
  httpInfo.pskey.value = pskey;
  httpInfo.appid.value = '537395438';
  httpInfo.env.value = 0;
  httpInfo.routeId.value = '0';
  req.httpReqInfo.value = httpInfo;
  var ext = NI.Ext.$new();
  var e1 = NI.Entry.$new(); e1.key.value = 'clientkey'; e1.value.value = CLIENT_KEY;
  var list = Java.use('java.util.ArrayList').$new(); list.add(e1);
  ext.mapInfoList.value = list;
  req.extInfo.value = ext;
  return { req: req, clientKey: CLIENT_KEY };
}

function publish(guildIdS, channelIdS, text, posterIdS) {
  init();
  return getPskey('pd.qq.com', 8000).then(function (pskey) {
    var built = buildReq(guildIdS, channelIdS, text, posterIdS, pskey);
    return new Promise(function (resolve) {
      var result = { idd: null, retCode: null, errMsg: null, gotRsp: false };
      g.pubCbHolder.result = result;
      var cb = g.pubCbInstance;
      var svc = Java.retain(g.feedService);
      var reqRef = Java.retain(built.req);
      send(TAG + 'publish ' + built.clientKey + ' (pskey len ' + pskey.length + ')');
      Java.scheduleOnMainThread(function () {
        try { svc.publishFeed(reqRef, cb); } catch (e) { send(TAG + 'publishFeed threw: ' + e); }
      });
      var waited = 0;
      var t = setInterval(function () {
        waited += 250;
        if (result.gotRsp || waited > 20000) {
          clearInterval(t);
          resolve(result);
        }
      }, 250);
    });
  });
}

// Pre-register callback singletons at script-load context (registerClass works here).
Java.perform(function () {
  g.pskeyCbHolder = { domain: null, v: null };
  g.pubCbHolder = { result: null };
  try {
    g.pskeyCbInstance = Java.registerClass({
      name: 'org.rea.NodePskeyCb',
      implements: [Java.use('db2.a')],
      methods: {
        onSuccess: function (map) {
          var h = g.pskeyCbHolder;
          try { h.v = String(map.get(h.domain)); } catch (e) { h.v = ''; }
        },
        onFail: function (msg) { g.pskeyCbHolder.v = ''; }
      }
    }).$new();
    g.pubCbInstance = Java.registerClass({
      name: 'org.rea.NodePubCb',
      implements: [Java.use('com.tencent.qqnt.kernelgpro.nativeinterface.IGPropublishFeedCallback')],
      methods: {
        onResult: function (code, msg, ok, rsp) {
          var r = g.pubCbHolder.result;
          if (!r) return;
          r.retCode = code;
          r.errMsg = String(msg);
          if (rsp !== null) {
            r.gotRsp = true;
            try { r.idd = String(rsp.feed.value.idd.value); } catch (e) {}
          }
        }
      }
    }).$new();
    send(TAG + 'callback singletons registered');
  } catch (e) {
    send(TAG + 'register singletons failed: ' + e);
  }
});

rpc.exports = {
  status: function () {
    init();
    return getPskey('pd.qq.com', 6000).then(function (pskey) {
      return { ready: true, pskeyLen: pskey.length };
    });
  },
  publish: function (guildIdS, channelIdS, text, posterIdS) {
    return publish(String(guildIdS), String(channelIdS), String(text), String(posterIdS));
  }
};

send(TAG + 'agent loaded');
