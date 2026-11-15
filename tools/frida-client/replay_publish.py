import frida, threading, sys
JS = open('/mnt/d/xiaomi-im-qq/tools/frida-client/replay_publish.js', encoding='utf-8').read()
got = threading.Event()
def on_message(m, d):
    p = str(m.get('payload')) if m.get('type') == 'send' else str(m)
    print(p, flush=True)
    if 'replay-result' in p or 'ERROR' in p:
        got.set()
device = frida.get_device_manager().add_remote_device('192.168.1.204:27042')
session = device.attach(15848)
sc = session.create_script(JS)
sc.on('message', on_message)
sc.load()
print('[py] armed, waiting for a real publish (max 300s)...', flush=True)
got.wait(900)
session.detach()
print('[py] done', flush=True)
