'use strict';
// Capture QQ guild feed publish: kernel publishFeed req/rsp + pskey credential.
Java.perform(function () {
  // 1) pskey credential for pd.qq.com (fires whenever app refreshes pskey)
  try {
    var ClsGProPskey = Java.use('com.tencent.qqnt.kernelgpro.nativeinterface.GProHttpReqInfo');
    console.log('[hook] GProHttpReqInfo ready');
  } catch (e) { console.log('[warn] GProHttpReqInfo: ' + e); }

  // 2) publishFeed entry — dump full request structure
  var Proxy = Java.use('com.tencent.qqnt.kernelgpro.nativeinterface.IKernelFeedServiceNew$CppProxy');
  Proxy.publishFeed.overloads.forEach(function (ov) {
    ov.implementation = function (req, cb) {
      try {
        var ts = new Date().toISOString();
        console.log('\n===== PUBLISH FEED REQ @ ' + ts + ' =====');
        console.log('[req.toString]\n' + req.toString());
        console.log('[httpReqInfo]\n' + req.httpReqInfo.value.toString());
        console.log('[jsonFeed]\n' + req.jsonFeed.value);
        // dump callback result too
        var CbCls = Java.use(cb.$className || 'com.tencent.qqnt.kernelgpro.nativeinterface.IGPropublishFeedCallback');
      } catch (e) { console.log('[req dump error] ' + e); }
      var wrapped = Java.registerClass({
        name: 'org.rea.CapturePublishCallback' + Date.now(),
        implements: [Java.use('com.tencent.qqnt.kernelgpro.nativeinterface.IGPropublishFeedCallback')],
        methods: {
          onResult: function (code, msg, ok, rsp) {
            try {
              console.log('\n===== PUBLISH FEED RSP =====');
              console.log('retCode=' + code + ' errMsg=' + msg + ' isSuccess=' + ok);
              if (rsp !== null) console.log('[rsp]\n' + rsp.toString());
            } catch (e) { console.log('[rsp dump error] ' + e); }
            return cb.onResult(code, msg, ok, rsp);
          }
        }
      }).$new();
      return this.publishFeed(req, wrapped);
    };
  });
  console.log('[hooked] publishFeed');

  // 3) alterFeed too (edit path)
  Proxy.alterFeed.overloads.forEach(function (ov) {
    ov.implementation = function (req, cb) {
      try {
        console.log('\n===== ALTER FEED REQ =====');
        console.log('[req.toString]\n' + req.toString());
      } catch (e) {}
      return this.alterFeed(req, cb);
    };
  });
  console.log('[hooked] alterFeed');

  // 4) pskey manager — capture credential without publishing
  try {
    var IPskeyMgr = Java.use('com.tencent.mobileqq.qqguildsdk.api.IPskeyManager');
    console.log('[info] IPskeyManager interface found');
  } catch (e) {}
  console.log('[capture script fully installed]');
});
