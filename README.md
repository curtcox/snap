# snap
Simple Network Announcement Protocol

## Usage

To send an announcement from the command line:
```
snap --send --topic "topic name" --message "message to send on the topic"
```

To listen for messages on a topic:
```
snap --listen "topic name"
```

Which will listen indefinitely and return topic responses as they are received:
```
topic bedroom/light/brightness
value 75
time 2022/03/31 06:49:34.2088
topic bedroom/light/brightness
value 0
time 2022/03/31 14:49:34.2088
```

You can specify a timeout:
```
snap --listen "topic name" --timeout 100s
```

### Wildcards

Topic names can contain arbitrary text. Wildcards (* and ?) can be used to partially specify a topic name
and thus match multiple topic names.

### Finding Listeners
To find all current listeners send a ping request.
```
snap --ping
```

Which will request that all current listeners identify themselves.
Any listener for the specified topic should reply with information about the listener and the topic they are listening to.

A topic and timeout can optionally be given.
If not specified, the topic being listened to defaults to * and the timeout defaults to 1s.

Ping requests aren't displayed by --listen, but can be displayed using either --listen-ping (for just ping requests)
or --listen-all (for ping requests and normal announcements).

### Defaults


## Protocol

