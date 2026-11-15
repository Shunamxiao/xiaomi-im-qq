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

function uuid4() {
  var h = '0123456789abcdef', s = '';
  for (var i = 0; i < 36; i++) {
    if (i === 8 || i === 13 || i === 18 || i === 23) s += '-';
    else if (i === 14) s += '4';
    else if (i === 19) s += h[(8 + (Math.random() * 4) | 0)];
    else s += h[(Math.random() * 16) | 0];
  }
  return s;
}

// 20-digit random id, mimics QQ session-id style (e.g. 17947238981717353972)
function bigSessionId() {
  var s = String(17000000000000000000 + Math.floor(Math.random() * 900000000000000000));
  while (s.length < 20) s = '1' + s;
  return s;
}

// Real-client extInfo audit (capture.log 2026-11-15): 10 entries incl. sec_flag_v1.
// Omitting these is a detectable fingerprint difference, so we mirror the native shape.
function buildExtInfo(NI, EntryCls, clientKey, nParas) {
  var entries = [
    ['clientkey', clientKey],
    ['re_page_id', 'ark_link_share'],
    ['sgrp_visit_from', 'pg_sgrp_channel_feed'],
    ['sgrp_invite_content_id', ''],
    ['sgrp_stream_pgin_source_name', 'msg_tab_list_channel'],
    ['sgrp_session_id', bigSessionId()],
    ['sgrp_source_miniprogram_appid', ''],
    ['sgrp_forum_visit_from', 'channel_feed_all'],
    ['sgrp_msg_tab_session', bigSessionId()],
    ['feed_source_json', '{"sgrp_forum_visit_from":"channel_feed_all"}'],
    ['sec_flag_v1', 'version=1&plt=1&is_enable=1&n_paste=0&text_similarity=0&is_draft=0&' +
      'n_text=' + (nParas * 3) + '&n_input_event=0&n_text_changed=' + (nParas * 3) +
      '&and_extra=ime_show%3D1%26port_check%3D0%26ime_key_check%3D0']
  ];
  var list = Java.use('java.util.ArrayList').$new();
  entries.forEach(function (e) {
    var en = EntryCls.$new();
    en.key.value = e[0];
    en.value.value = e[1];
    list.add(en);
  });
  return list;
}

function buildReq(guildIdS, channelIdS, text, posterIdS, pskey, images, paragraphs) {
  var NI = g.NI;
  var JLong = Java.use('java.lang.Long');
  // Native format: GuildNativeMain_<ms>_<uuid4> (capture.log). 'NodeSvc_' style is a giveaway.
  var CLIENT_KEY = 'GuildNativeMain_' + Date.now() + '_' + uuid4();
  var imgList = images || [];
  var paras = paragraphs && paragraphs.length ? paragraphs
    : [{ elems: [{ text: { text_content: { text: text } }, type: 1 }] }];
  var nParas = paras.length;
  var sign = NI.Sign.$new();
  sign.guildId.value = JLong.parseLong(guildIdS);
  sign.channelId.value = JLong.parseLong(channelIdS);
  var chanInfo = NI.ChanInfo.$new();
  chanInfo.sign.value = sign;
  // Real client sends isSquare=false on the proto object (jsonFeed keeps is_square:true).
  chanInfo.isSquare.value = false;
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
    tagInfos: [], recommend_channels: [], images: imgList, videos: [],
    client_task_id: CLIENT_KEY,
    media_lock_count: 0,
    content_with_style: { paragraphs: paras },
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
  ext.mapInfoList.value = buildExtInfo(NI, NI.Entry, CLIENT_KEY, nParas);
  req.extInfo.value = ext;
  return { req: req, clientKey: CLIENT_KEY };
}

// Simple anti-risk throttle: minimum interval between publishes (ms).
var MIN_PUBLISH_INTERVAL = 10000;
var lastPublishAt = 0;

function publish(guildIdS, channelIdS, text, posterIdS, images, paragraphs) {
  init();
  var gap = Date.now() - lastPublishAt;
  if (gap < MIN_PUBLISH_INTERVAL) {
    return Promise.resolve({ idd: null, retCode: -1000, errMsg: 'rate limited, retry in ' + Math.ceil((MIN_PUBLISH_INTERVAL - gap) / 1000) + 's', gotRsp: false });
  }
  lastPublishAt = Date.now();
  return getPskey('pd.qq.com', 8000).then(function (pskey) {
    var built = buildReq(guildIdS, channelIdS, text, posterIdS, pskey, images, paragraphs);
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
  credentials: function () {
    init();
    var uin = '';
    try { uin = String(g.appRuntime.getCurrentUin()); } catch (e) {}
    if (!uin) {
      try { uin = String(g.appRuntime.getAccount()); } catch (e) {}
    }
    var tinyid = '144115219939370567';
    try {
      var u = g.fGuild.use('com.tencent.mobileqq.guild.util.bq');
    } catch (e) {}
    return getPskey('pd.qq.com', 6000).then(function (pskey) {
      return { pskey: pskey, uin: uin, tinyid: tinyid };
    });
  },
  publish: function (guildIdS, channelIdS, text, posterIdS, images, paragraphs) {
    return publish(String(guildIdS), String(channelIdS), String(text), String(posterIdS), images || [], paragraphs || null);
  }
};

send(TAG + 'agent loaded');
