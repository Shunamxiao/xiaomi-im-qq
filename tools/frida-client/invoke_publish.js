'use strict';
// Self-invoke kernel publishFeed with a hand-built request.
Java.perform(function () {
  var TAG = '[invoke] ';
  var CLIENT_KEY = 'ReaTest_' + Date.now() + '_' + Math.random().toString(16).slice(2, 10);
  var GUILD_ID_S = '641198144054265445';
  var CHANNEL_ID = '703765655';
  var POSTER_ID = '144115219939370567';

  // locate class factories
  var K = Java.use('com.tencent.qqnt.kernelgpro.nativeinterface.GProStPublishFeedReq'); // default loader has kernelgpro
  var fGuild = null, fKernelLoader = null;
  Java.enumerateClassLoadersSync().forEach(function (ld) {
    try {
      var f = Java.ClassFactory.get(ld);
      if (!fGuild) { try { f.use('com.tencent.mobileqq.guild.util.dm'); fGuild = f; } catch (e) {} }
    } catch (e) {}
  });
  if (!fGuild) { send(TAG + 'no guild loader'); return; }
  var dm = fGuild.use('com.tencent.mobileqq.guild.util.dm');
  var IGProSession = fGuild.use('com.tencent.mobileqq.qqguildsdk.api.IGProSession');
  var IPskeyManager = fGuild.use('com.tencent.mobileqq.pskey.api.IPskeyManager');

  var rt = dm.N();
  if (rt === null) { send(TAG + 'no appRuntime'); return; }
  send(TAG + 'rt=' + rt.getClass().getName());
  var session = Java.cast(rt.getRuntimeService(IGProSession.class, ''), IGProSession);
  send(TAG + 'session isInit=' + session.isInit());
  if (!session.isInit()) { send(TAG + 'session not ready'); return; }
  var feedService = session.getFeedService();
  send(TAG + 'feedService ok');

  // kernelgpro classes via default loader
  var NI = {
    Req: Java.use('com.tencent.qqnt.kernelgpro.nativeinterface.GProStPublishFeedReq'),
    Sign: Java.use('com.tencent.qqnt.kernelgpro.nativeinterface.GProStChannelSign'),
    ChanInfo: Java.use('com.tencent.qqnt.kernelgpro.nativeinterface.GProStChannelInfo'),
    Feed: Java.use('com.tencent.qqnt.kernelgpro.nativeinterface.GProStFeed'),
    HttpInfo: Java.use('com.tencent.qqnt.kernelgpro.nativeinterface.GProHttpReqInfo'),
    Ext: Java.use('com.tencent.qqnt.kernelgpro.nativeinterface.GProStCommonExt'),
    Entry: Java.use('com.tencent.qqnt.kernelgpro.nativeinterface.GProFeedEntry'),
    PubCb: Java.use('com.tencent.qqnt.kernelgpro.nativeinterface.IGPropublishFeedCallback')
  };

  var pskeyHolder = { v: null };
  var pskeyCb = Java.registerClass({
    name: 'org.rea.PskeyCb' + Date.now(),
    implements: [Java.use('db2.a')],
    methods: {
      onSuccess: function (map) {
        try { pskeyHolder.v = String(map.get('pd.qq.com')); send(TAG + 'pskey raw=' + pskeyHolder.v.slice(0,12) + '... len=' + pskeyHolder.v.length); } catch (e) { send(TAG + 'pskey err ' + e); }
      },
      onFail: function (msg) { send(TAG + 'pskey FAIL: ' + msg); }
    }
  }).$new();
  var pskeyMgr = Java.cast(rt.getRuntimeService(IPskeyManager.class, ''), IPskeyManager);
  pskeyMgr.getPskey(['pd.qq.com'], pskeyCb);

  var waited = 0;
  var timer = setInterval(function () {
    waited += 500;
    if (pskeyHolder.v || waited > 10000) {
      clearInterval(timer);
      doPublish(pskeyHolder.v || '');
    }
  }, 500);

  function doPublish(pskey) {
    send(TAG + 'doPublish entered, pskeyLen=' + pskey.length);
    try {
      send(TAG + 'building req...');
      var JLong = Java.use('java.lang.Long');
      var sign = NI.Sign.$new();
      sign.guildId.value = JLong.parseLong(GUILD_ID_S);
      sign.channelId.value = JLong.parseLong(CHANNEL_ID);
      var chanInfo = NI.ChanInfo.$new();
      chanInfo.sign.value = sign;
      chanInfo.isSquare.value = true;
      var feed = NI.Feed.$new();
      feed.channelInfo.value = chanInfo;
      feed.clientTaskId.value = CLIENT_KEY;
      feed.feedType.value = 1;
      var req = NI.Req.$new();
      req.feed.value = feed;
      req.jsonFeed.value = JSON.stringify({
        feed_type: 1,
        title: {},
        poster: { id: POSTER_ID },
        channelInfo: { sign: { guild_id: GUILD_ID_S, channel_id: CHANNEL_ID }, is_square: true },
        tagInfos: [], recommend_channels: [], images: [], videos: [],
        client_task_id: CLIENT_KEY,
        media_lock_count: 0,
        content_with_style: { paragraphs: [{ elems: [{ text: { text_content: { text: 'rea-node-protocol-test ' + new Date().toISOString() } }, type: 1 }] }] },
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
      var JArrayList = Java.use('java.util.ArrayList');
      var list = JArrayList.$new();
      list.add(e1);
      ext.mapInfoList.value = list;
      req.extInfo.value = ext;
      send(TAG + 'req built, clientkey=' + CLIENT_KEY + ' pskeyLen=' + pskey.length);

      var pubCb = Java.registerClass({
        name: 'org.rea.PubCb' + Date.now(),
        implements: [NI.PubCb],
        methods: {
          onResult: function (code, msg, ok, rsp) {
            send('\n[invoke-result] retCode=' + code + ' errMsg=' + msg + ' ok=' + ok);
            if (rsp !== null) {
              try { send('[invoke-result] feed idd=' + rsp.feed.value.idd.value); }
              catch (e) { send('[invoke-result] rsp partial: ' + e); }
            }
          }
        }
      }).$new();
      Java.scheduleOnMainThread(function () {
        send(TAG + 'publishing on main thread...');
        feedService.publishFeed(req, pubCb);
      });
      send(TAG + 'publishFeed invoked');
    } catch (e) {
      send(TAG + 'error: ' + e + '\n' + (e.stack || ''));
    }
  }
});
