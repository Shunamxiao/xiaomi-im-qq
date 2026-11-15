# -*- coding: utf-8 -*-
import frida, time

JS = open('/mnt/d/xiaomi-im-qq/tools/frida-client/capture_upload.js', 'r', encoding='utf-8').read()
LOG = open('/mnt/d/xiaomi-im-qq/tools/frida-client/capture_upload.log', 'w', encoding='utf-8', buffering=1)

def log(s):
    LOG.write(s + '\n')

def on_message(message, data):
    if message.get('type') == 'send':
        log('[msg] ' + str(message.get('payload')))
    elif message.get('type') == 'error':
        log('[err] ' + str(message))

device = frida.get_device_manager().add_remote_device('192.168.1.204:27042')
pid = None
for p in device.enumerate_processes(scope='full'):
    if (getattr(p, 'identifier', '') == 'com.tencent.mobileqq') or p.name in ('QQ', 'com.tencent.mobileqq'):
        pid = p.pid
        break
log('[py] pid=' + str(pid))
session = device.attach(pid)
script = session.create_script(JS)
script.on('message', on_message)
script.load()
log('[py] armed, waiting up to 300s for a real image upload...')
time.sleep(300)
session.detach()
log('[py] done')
