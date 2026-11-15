'use strict';
Java.perform(function () {
  var TAG = '[sso] ';
  var TSM = Java.use('com.tencent.qphone.base.remote.ToServiceMsg');
  TSM.setServiceCmd.overloads.forEach(function (ov) {
    ov.implementation = function (cmd) {
      try {
        var c = String(cmd);
        if (c.indexOf('trpc') !== -1 || c.indexOf('QChannel') !== -1 || c.indexOf('Feed') !== -1) {
          send(TAG + 'SET-CMD: ' + c);
          try {
            var wup = this.getWupBuffer();
            if (wup !== null) {
              var bytes = Java.array('byte', wup.bytes ? wup.bytes() : []);
              var hexs = '';
              var arr = wup.bytes();
              for (var i = 0; i < Math.min(arr.length, 2048); i++) { var h = (arr[i] & 0xff).toString(16); hexs += (h.length < 2 ? '0' : '') + h; }
              send(TAG + 'cmd=' + c + ' wupLen=' + arr.length + ' wupHex=' + hexs);
            }
          } catch (e) { send(TAG + 'wup err ' + e); }
        }
      } catch (e) {}
      return this.setServiceCmd(cmd);
    };
  });
  send(TAG + 'setServiceCmd hooked');
});
