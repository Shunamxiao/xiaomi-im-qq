'use strict';
// Passive probe: verify kernel feed service classes exist in QQ process.
Java.perform(function () {
  var candidates = [
    'com.tencent.qqnt.kernelgpro.nativeinterface.IKernelFeedServiceNew$CppProxy',
    'com.tencent.qqnt.kernelgpro.nativeinterface.IKernelFeedServiceNew',
    'com.tencent.qqnt.kernel.api.s',
    'com.tencent.qqnt.kernelgpro.nativeinterface.GProStPublishFeedReq',
    'com.tencent.qqnt.kernelgpro.nativeinterface.GProStFeed'
  ];
  candidates.forEach(function (name) {
    try {
      var cl = Java.use(name);
      console.log('[+] FOUND ' + name);
      var overloads = cl.publishFeed ? cl.publishFeed.overloads : [];
      overloads.forEach(function (ov) { console.log('    publishFeed(' + ov.argumentTypes.map(function (t) { return t.className; }).join(', ') + ')'); });
    } catch (e) {
      console.log('[-] missing ' + name);
    }
  });
  console.log('[probe done]');
});
