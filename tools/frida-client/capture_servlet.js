'use strict';
Java.perform(function () {
  var TAG = '[servlet] ';
  var f = null;
  Java.enumerateClassLoadersSync().forEach(function (ld) {
    if (f) return;
    try { var ff = Java.ClassFactory.get(ld); ff.use('com.tencent.qqnt.kernel.api.IServletAPI'); f = ff; } catch (e) {}
  });
  if (!f) { send(TAG + 'no IServletAPI loader'); return; }
  var IServletAPI = f.use('com.tencent.qqnt.kernel.api.IServletAPI');
  var QRoute = f.use('com.tencent.mobileqq.qroute.QRoute');
  var impl = QRoute.api(IServletAPI.class);
  send(TAG + 'impl=' + impl.getClass().getName());
  var cls = f.use(impl.getClass().getName());
  function hex(arr, max) {
    var bytes = arr.bytes(); var s = '';
    for (var i = 0; i < Math.min(bytes.length, max || 4096); i++) { var h = (bytes[i] & 0xff).toString(16); s += (h.length < 2 ? '0' : '') + h; }
    return s;
  }
  cls.maybeMockNerveCordSSO.overloads.forEach(function (ov) {
    ov.implementation = function (requestId, cmd, body) {
      try {
        var c = String(cmd);
        if (c.indexOf('PublishFeed') !== -1 || c.indexOf('ComWriter') !== -1 || c.indexOf('qchannel') !== -1) {
          send(TAG + 'REQ id=' + requestId + ' cmd=' + c + ' bodyLen=' + (body === null ? 0 : body.length));
          if (body !== null) send(TAG + 'REQ-HEX ' + hex(Java.array('byte', body.bytes ? body.bytes() : [])));
        }
      } catch (e) { send(TAG + 'req err ' + e); }
      return ov.apply(this, arguments);
    };
  });
  cls.onNerveCordSSOReply.overloads.forEach(function (ov) {
    ov.implementation = function (requestId, cmd, body, ssoRet, trpcRet, trpcFuncCode, errorMsg) {
      try {
        var c = String(cmd);
        if (c.indexOf('PublishFeed') !== -1 || c.indexOf('ComWriter') !== -1 || c.indexOf('qchannel') !== -1) {
          send(TAG + 'RSP id=' + requestId + ' cmd=' + c + ' ssoRet=' + ssoRet + ' trpcRet=' + trpcRet + ' func=' + trpcFuncCode + ' err=' + errorMsg);
          if (body !== null) send(TAG + 'RSP-HEX ' + hex(Java.array('byte', body.bytes ? body.bytes() : [])));
        }
      } catch (e) { send(TAG + 'rsp err ' + e); }
      return ov.apply(this, arguments);
    };
  });
  send(TAG + 'hooked on ' + impl.getClass().getName());
});
