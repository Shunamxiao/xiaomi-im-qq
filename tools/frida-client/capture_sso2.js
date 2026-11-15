'use strict';
Java.perform(function () {
  var TAG = '[sso2] ';
  var TSM = Java.use('com.tencent.qphone.base.remote.ToServiceMsg');
  TSM.$init.overloads.forEach(function (ov) {
    ov.implementation = function () {
      var args = Array.prototype.slice.call(arguments);
      var s = args.map(function (a) { return a === null ? 'null' : String(a); }).join(' | ');
      if (s.indexOf('trpc') !== -1 || s.indexOf('QChannel') !== -1 || s.indexOf('Feed') !== -1 || s.indexOf('Guild') !== -1) {
        send(TAG + 'CTOR: ' + s.slice(0, 200));
      }
      return ov.apply(this, arguments);
    };
  });
  send(TAG + 'ctor hooked');
  // also hook MSF SDK send
  try {
    var MsfSdk = Java.use('com.tencent.mobileqq.msf.sdk.MsfServiceSdk');
    MsfSdk.sendMsg.overloads.forEach(function (ov) {
      ov.implementation = function (msg) {
        try {
          if (msg !== null) {
            var cmd = String(msg.getServiceCmd());
            if (cmd.indexOf('trpc') !== -1 || cmd.indexOf('QChannel') !== -1) {
              send(TAG + 'SEND: ' + cmd);
              var wup = msg.getWupBuffer();
              if (wup !== null) {
                var arr = wup.bytes();
                var hexs = '';
                for (var i = 0; i < Math.min(arr.length, 4096); i++) { var h = (arr[i] & 0xff).toString(16); hexs += (h.length < 2 ? '0' : '') + h; }
                send(TAG + 'cmd=' + cmd + ' wupLen=' + arr.length + ' hex=' + hexs);
              }
            }
          }
        } catch (e) { send(TAG + 'err ' + e); }
        return ov.apply(this, arguments);
      };
    });
    send(TAG + 'MsfServiceSdk.sendMsg hooked');
  } catch (e) { send(TAG + 'msf sdk hook fail: ' + e); }
});
