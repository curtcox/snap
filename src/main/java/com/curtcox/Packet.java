package com.curtcox;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import static com.curtcox.Check.notNull;

final class Packet {
    public static final Bytes MAGIC;

    static {
        try {
            MAGIC = new Bytes("snap".getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

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
