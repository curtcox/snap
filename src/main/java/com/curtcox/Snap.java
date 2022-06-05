package com.curtcox;

import static com.curtcox.Check.notNull;

// Potential future API
public final class Snap {

    final Node node;

    Snap(Node node) {
        this.node = notNull(node);
    }

    public void send(String topic, String message) {
        node.write(new Packet(topic,message));
    }

    public Packet listen(String topic) {
        return node.read(topic);
    }

    public Packet listen() {
        return node.read();
    }

}
