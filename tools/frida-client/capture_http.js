'use strict';
// Capture plaintext gotrpc HTTP requests via SSL_write in libbasic_share.so.
var capturedFds = {};   // ssl ptr -> expiry ts of interest
var base = Module.findBaseAddress('libbasic_share.so');
if (!base) { console.log('[!] libbasic_share.so not loaded yet'); }
var sslWrite = Module.findExportByName('libbasic_share.so', 'SSL_write');
var sslRead = Module.findExportByName('libbasic_share.so', 'SSL_read');
var sslGetFd = Module.findExportByName('libbasic_share.so', 'SSL_get_fd');
console.log('[*] SSL_write=' + sslWrite + ' SSL_read=' + sslRead);

if (sslWrite) {
  Interceptor.attach(sslWrite, {
    onEnter: function (args) {
      try {
        var buf = args[1]; var num = args[2].toInt32();
        if (num <= 0 || num > 262144) return;
        var head = Memory.readUtf8String(Math.min(num, 512));
        if (head && head.indexOf('gotrpc') !== -1 && head.indexOf('HTTP/') !== -1) {
          var ssl = args[0];
          capturedFds[ssl] = Date.now() + 15000;
          var data = Memory.readByteArray(buf, num);
          console.log('\n########## GOTRPC HTTP REQUEST (' + num + ' bytes) ##########');
          console.log(hexdumpish(data));
        }
      } catch (e) {}
    }
  });
}
if (sslRead && sslGetFd) {
  Interceptor.attach(sslRead, {
    onEnter: function (args) { this.ssl = args[0]; this.buf = args[1]; },
    onLeave: function (retval) {
      try {
        var n = retval.toInt32();
        if (n <= 0) return;
        var exp = capturedFds[this.ssl];
        if (!exp || Date.now() > exp) return;
        var data = Memory.readByteArray(this.buf, Math.min(n, 65536));
        console.log('\n########## GOTRPC HTTP RESPONSE (' + n + ' bytes) ##########');
        console.log(hexdumpish(data));
        capturedFds[this.ssl] = Date.now() + 5000;
      } catch (e) {}
    }
  });
}
function hexdumpish(data) {
  var bytes = new Uint8Array(data);
  var text = '';
  for (var i = 0; i < bytes.length; i++) {
    var c = bytes[i];
    text += (c >= 32 && c < 127) ? String.fromCharCode(c) : '.';
  }
  return text;
}

// Retain publishFeed service instance + last request for possible direct invocation.
Java.perform(function () {
  var Proxy = Java.use('com.tencent.qqnt.kernelgpro.nativeinterface.IKernelFeedServiceNew$CppProxy');
  Proxy.publishFeed.overloads.forEach(function (ov) {
    ov.implementation = function (req, cb) {
      global.lastService = Java.retain(this);
      global.lastReq = Java.retain(req);
      console.log('[publishFeed called] instance retained');
      return this.publishFeed(req, cb);
    };
  });
  console.log('[hooked] publishFeed (instance retention)');
});
