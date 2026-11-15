'use strict';
// Hook createFileGroupUpload to capture REAL upload params (fileId/path format).
Java.perform(function () {
  var found = null;
  Java.enumerateClassLoadersSync().forEach(function (ld) {
    if (found) return;
    try {
      var f = Java.ClassFactory.get(ld);
      var dm = f.use('com.tencent.mobileqq.guild.util.dm');
      if (dm.N() !== null) found = f;
    } catch (e) {}
  });
  if (!found) { send('[cap-up] no runtime'); return; }
  var GW = found.use('com.tencent.mobileqq.qqguildsdk.api.impl.GuildFileTransferService');
  var svc = null;
  try {
    var GProSessionImpl = found.use('com.tencent.mobileqq.qqguildsdk.api.impl.GProSessionImpl');
    var IGProSession = found.use('com.tencent.mobileqq.qqguildsdk.api.IGProSession');
    var rt = found.use('com.tencent.mobileqq.guild.util.dm').N();
    var sess = Java.cast(rt.getRuntimeService(IGProSession.class, ''), IGProSession);
    var impl = Java.cast(sess, GProSessionImpl);
    svc = impl.getClass().getName().indexOf('GProSessionImpl') >= 0 ? sess : null;
  } catch (e) { send('[cap-up] ' + e); }
  // hook the wrapper class method (instance hooked lazily via class-level)
  try {
    var GWc = found.use('com.tencent.mobileqq.qqguildsdk.api.impl.GuildFileTransferService');
    GWc.createFileGroupUpload.implementation = function (param, cb) {
      send('\n===== REAL createFileGroupUpload =====');
      send('param=' + param);
      try { send('groupInfo=' + param.fileGroupInfo.value); } catch (e) {}
      try {
        var it = param.fileParams.value.iterator();
        while (it.hasNext()) { send('fileParam=' + it.next()); }
      } catch (e) { send('fileParams err ' + e); }
      return this.createFileGroupUpload(param, cb);
    };
    send('[cap-up] hook installed on GuildFileTransferService.createFileGroupUpload');
  } catch (e) { send('[cap-up] hook err ' + e); }
});
