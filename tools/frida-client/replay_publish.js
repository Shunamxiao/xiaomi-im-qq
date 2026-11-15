'use strict';
// Hook real publishFeed; on capture, clone the req with fresh clientkey and replay it.
Java.perform(function () {
  var TAG = '[replay] ';
  var Proxy = Java.use('com.tencent.qqnt.kernelgpro.nativeinterface.IKernelFeedServiceNew$CppProxy');
  var GProFeedEntry = Java.use('com.tencent.qqnt.kernelgpro.nativeinterface.GProFeedEntry');
  var JArrayList = Java.use('java.util.ArrayList');
  var done = false;

  Proxy.publishFeed.overloads.forEach(function (ov) {
    ov.implementation = function (req, cb) {
      if (!done) {
        done = true;
        try {
          var svc = Java.retain(this);
          var orig = Java.retain(req);
          send(TAG + 'captured real publish, replaying...');
          setTimeout(function () { doReplay(svc, orig); }, 1500);
        } catch (e) { send(TAG + 'capture err ' + e); }
      }
      return this.publishFeed(req, cb);
    };
  });
  send(TAG + 'hook armed');

  function doReplay(svc, orig) {
    try {
      var JLong = Java.use('java.lang.Long');
      var CK = 'ReaReplay_' + Date.now() + '_' + Math.random().toString(16).slice(2, 8);
      // clone: jsonFeed with new client_task_id
      var jf = String(orig.jsonFeed.value).replace(/"client_task_id":"[^"]*"/g, '"client_task_id":"' + CK + '"');
      var feed = orig.feed.value;
      var newFeed = Java.retain(feed); // reuse same feed struct
      newFeed.clientTaskId.value = CK;
      // extInfo: rebuild with new clientkey
      var origExt = orig.extInfo.value;
      var list = JArrayList.$new();
      var it = origExt.mapInfoList.value.iterator();
      while (it.hasNext()) {
        var en = Java.cast(it.next(), GProFeedEntry);
        var ne = GProFeedEntry.$new();
        ne.key.value = String(en.key.value);
        ne.value.value = String(en.value.value).indexOf('GuildNativeMain_') === 0 ? CK : String(en.value.value);
        list.add(ne);
      }
      origExt.mapInfoList.value = list;
      send(TAG + 'replay req ready, clientkey=' + CK);
      var PubCb = Java.use('com.tencent.qqnt.kernelgpro.nativeinterface.IGPropublishFeedCallback');
      var cb2 = Java.registerClass({
        name: 'org.rea.ReplayCb' + Date.now(),
        implements: [PubCb],
        methods: {
          onResult: function (code, msg, ok, rsp) {
            send('[replay-result] retCode=' + code + ' errMsg=' + msg + ' ok=' + ok);
            if (rsp !== null) {
              try { send('[replay-result] feed idd=' + rsp.feed.value.idd.value); }
              catch (e) { send('[replay-result] rsp=' + String(rsp.toString()).slice(0, 300)); }
            }
          }
        }
      }).$new();
      svc.publishFeed(orig, cb2);
      send(TAG + 'replay invoked');
    } catch (e) {
      send(TAG + 'ERROR ' + e + ' stack ' + (e.stack || ''));
    }
  }
});
