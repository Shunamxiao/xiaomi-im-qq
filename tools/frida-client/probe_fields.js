Java.perform(function () {
  var Req = Java.use('com.tencent.qqnt.kernelgpro.nativeinterface.GProStPublishFeedReq');
  var r = Req.$new();
  var names = Object.getOwnPropertyNames(Object.getPrototypeOf(r)).concat(Object.getOwnPropertyNames(r));
  send('[probe] own props: ' + Object.getOwnPropertyNames(r).join(','));
  send('[probe] proto props: ' + Object.getOwnPropertyNames(Object.getPrototypeOf(r)).slice(0, 60).join(','));
  send('[probe] r.feed=' + r.feed + ' r.feed.value=' + (r.feed ? r.feed.value : 'nofield'));
});
