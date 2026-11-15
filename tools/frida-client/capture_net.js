'use strict';
// Wide net observation: SSL_write heads + connect destinations + gotrpc bodies.
function readHead(buf, num) {
  var bytes = Memory.readByteArray(buf, Math.min(num, 400));
  var u = new Uint8Array(bytes); var s = '';
  for (var i = 0; i < u.length; i++) s += (u[i] >= 32 && u[i] < 127) ? String.fromCharCode(u[i]) : '.';
  return s;
}
var sslWrite = Module.findExportByName('libbasic_share.so', 'SSL_write');
var sslRead = Module.findExportByName('libbasic_share.so', 'SSL_read');
var writes = {};
if (sslWrite) {
  Interceptor.attach(sslWrite, {
    onEnter: function (args) {
      try {
        var num = args[2].toInt32();
        if (num <= 0) return;
        var head = readHead(args[1], num);
        var ssl = args[0].toString();
        writes[ssl] = head;
        if (head.indexOf('gotrpc') !== -1 || head.indexOf('pd.qq.com') !== -1) {
          console.log('\n##### SSL_WRITE(gotrpc!) ' + num + ' bytes:\n' + readHead(args[1], Math.min(num, 4096)));
          if (num > 4096) {
            console.log('[body hex first 512]:\n' + hex(Memory.readByteArray(args[1].add(0), Math.min(num, 512))));
          }
          global.gotrpcSsl = ssl; global.gotrpcUntil = Date.now() + 20000;
        } else if (head.indexOf('POST ') === 0 || head.indexOf('GET ') === 0) {
          console.log('[ssl-write http] ' + head.slice(0, 200).replace(/\r/g, ' '));
        }
      } catch (e) {}
    }
  });
}
if (sslRead) {
  Interceptor.attach(sslRead, {
    onEnter: function (args) { this.ssl = args[0]; this.buf = args[1]; },
    onLeave: function (retval) {
      try {
        var n = retval.toInt32(); if (n <= 0) return;
        if (global.gotrpcSsl === this.ssl.toString() && Date.now() < global.gotrpcUntil) {
          console.log('\n##### SSL_READ(gotrpc) ' + n + ' bytes:\n' + readHead(this.buf, Math.min(n, 4096)));
        }
      } catch (e) {}
    }
  });
}
// connect() destinations
Interceptor.attach(Module.findExportByName('libc.so', 'connect'), {
  onEnter: function (args) {
    try {
      var fd = args[0].toInt32(); var sa = args[1];
      var family = Memory.readU16(sa);
      if (family === 2) { // AF_INET
        var port = (Memory.readU8(sa.add(2)) << 8) | Memory.readU8(sa.add(3));
        var ip = Memory.readU8(sa.add(4)) + '.' + Memory.readU8(sa.add(5)) + '.' + Memory.readU8(sa.add(6)) + '.' + Memory.readU8(sa.add(7));
        console.log('[connect] ' + ip + ':' + port);
      }
    } catch (e) {}
  }
});
function hex(data) {
  var u = new Uint8Array(data); var s = '';
  for (var i = 0; i < u.length; i++) { var h = u[i].toString(16); s += (h.length < 2 ? '0' : '') + h; }
  return s;
}
console.log('[net-observe installed]');
