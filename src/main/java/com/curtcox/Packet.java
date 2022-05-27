package com.curtcox;

import static com.curtcox.Check.notNull;

public final class Packet {
    public final String topic;
    public final String message;

    public Packet(String topic, String message) {
        this.topic = notNull(topic);
        this.message = notNull(message);
    }

    @Override
    public String toString() {
       return topic + " " + message;
    }
}
