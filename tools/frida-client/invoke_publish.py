# -*- coding: utf-8 -*-
import frida, sys, time, threading

JS = open('/mnt/d/xiaomi-im-qq/tools/frida-client/invoke_publish.js', 'r', encoding='utf-8').read()
done = threading.Event()

def on_message(message, data):
    if message.get('type') == 'send':
        print('[msg] ' + str(message.get('payload')), flush=True)
        pass
    elif message.get('type') == 'error':
        print('[err] ' + str(message), flush=True)
        done.set()

device = frida.get_device_manager().add_remote_device('192.168.1.204:27042')
session = device.attach(15848)
script = session.create_script(JS)
script.on('message', on_message)
script.load()
print('[py] script loaded', flush=True)
# wait up to 90s for result
for _ in range(300):
    if done.wait(0.1):
        break
print('[py] detaching', flush=True)
session.detach()
print('[py] done', flush=True)
