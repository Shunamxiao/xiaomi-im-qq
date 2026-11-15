# -*- coding: utf-8 -*-
"""WSL-side frida bridge host.

Runs inside WSL, attaches frida to the QQ main process on mi8, loads agent.js
and exposes a line-based JSON-RPC over stdin/stdout:

  request : {"id": <n>, "action": "<rpc name>", "args": [...]}
  response: {"id": <n>, "ok": true, "result": ...} | {"id": <n>, "ok": false, "error": "..."}
  event   : {"event": "log", "data": "..."}
"""
import frida
import json
import sys
import threading

DEVICE = '192.168.1.204:27042'
PROCESS = 'com.tencent.mobileqq'
AGENT = '/mnt/d/xiaomi-im-qq/qq-channel-service/bridge/agent.js'

script = None
lock = threading.Lock()


def on_message(message, data):
    if message.get('type') == 'send':
        emit_event(str(message.get('payload')))
    elif message.get('type') == 'error':
        emit_event('agent error: ' + str(message))


def emit_event(text):
    sys.stdout.write(json.dumps({'event': 'log', 'data': text}, ensure_ascii=False) + '\n')
    sys.stdout.flush()


def resolve_pid(dev):
    for proc in dev.enumerate_processes(scope='full'):
        ident = getattr(proc, 'identifier', '') or ''
        if ident == PROCESS or proc.name == PROCESS or proc.name == 'QQ':
            return proc.pid
    return None


def attach():
    global script
    dev = frida.get_device_manager().add_remote_device(DEVICE)
    pid = resolve_pid(dev)
    if pid is None:
        raise RuntimeError('QQ process not found on ' + DEVICE)
    session = dev.attach(pid)
    js = open(AGENT, 'r', encoding='utf-8').read()
    script = session.create_script(js)
    script.on('message', on_message)
    script.load()
    emit_event('attached %s and agent loaded' % PROCESS)


def main():
    attach()
    for line in sys.stdin:
        line = line.strip()
        if not line:
            continue
        try:
            req = json.loads(line)
        except ValueError:
            continue
        rid = req.get('id')
        action = req.get('action', '')
        args = req.get('args', [])
        try:
            with lock:
                result = getattr(script.exports_sync, action)(*args)
            sys.stdout.write(json.dumps({'id': rid, 'ok': True, 'result': result}, ensure_ascii=False) + '\n')
        except Exception as e:  # noqa: BLE001
            sys.stdout.write(json.dumps({'id': rid, 'ok': False, 'error': str(e)}, ensure_ascii=False) + '\n')
        sys.stdout.flush()


if __name__ == '__main__':
    main()
