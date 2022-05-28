package com.curtcox;

import java.io.IOException;

import static com.curtcox.Check.notNull;

final class Packet {
    public final String topic;
    public final String message;

    interface Receiver {
        void receive(Packet packet);
    }

    interface Reader {
        Packet read() throws IOException;
    }

    interface Writer {
        void write(Packet packet) throws IOException;
    }

    interface IO extends Reader, Writer {}

    public Packet(String topic, String message) {
        this.topic = notNull(topic);
        this.message = notNull(message);
    }

    @Override
    public String toString() {
       return topic + " " + message;
    }
}
