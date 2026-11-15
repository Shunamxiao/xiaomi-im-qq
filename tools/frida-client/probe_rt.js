'use strict';
Java.perform(function () {
  var TAG = '[probe] ';
  var fDm = null;
  Java.enumerateClassLoadersSync().forEach(function (ld) {
    if (fDm) return;
    try { var f = Java.ClassFactory.get(ld); f.use('com.tencent.mobileqq.guild.util.dm'); fDm = f; } catch (e) {}
  });
  console.log(TAG + 'dm factory: ' + (fDm ? 'yes' : 'no'));
  if (!fDm) return;
  var rt = fDm.use('com.tencent.mobileqq.guild.util.dm').N();
  console.log(TAG + 'rt=' + rt);
  if (rt === null) return;
  try {
    var cls = rt.getClass();
    console.log(TAG + 'class=' + cls.getName());
    var ms = cls.getMethods();
    for (var i = 0; i < ms.length; i++) {
      var n = ms[i].getName();
      if (n.indexOf('RuntimeService') >= 0 || n === 'peekAppRuntime') console.log(TAG + 'm: ' + ms[i].toString());
    }
  } catch (e) { console.log(TAG + 'err ' + e); }
});
