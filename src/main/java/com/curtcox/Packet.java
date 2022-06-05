package com.curtcox;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static com.curtcox.Check.notNull;

final class Packet {
    public static final Bytes MAGIC = new Bytes("snap".getBytes(StandardCharsets.UTF_8));

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

    public boolean equals(Object o) {
        Packet that  = (Packet) o;
        return topic.equals(that.topic) && message.equals(that.message);
    }

    public int hashCode() {
        return topic.hashCode() ^ message.hashCode();
    }

    @Override
    public String toString() {
       return topic + " " + message;
    }
}
