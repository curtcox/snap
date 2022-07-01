package com.curtcox;

import java.net.*;

import static com.curtcox.Check.notNull;

// Potential future API
public final class Snap {

    final Node node;
    private String name;

    Snap(Node node) {
        this.node = notNull(node);
    }

    public void send(String topic, String message) {
        node.write(new Packet(topic,message));
    }

    public Packet.Reader listen(String topic) {
        return node.read(topic);
    }

    public Packet.Reader listen() {
        return node.read();
    }


    public String whoami() {
        if (name==null) {
            return defaultName();
        }
        return name;
    }

    private String defaultName() {
        return user() + "@" + host();
    }

    String user() {
        return System.getProperty("user.name");
    }

    String host() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public void ping() {

    }
}
