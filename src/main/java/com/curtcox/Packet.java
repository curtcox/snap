package com.curtcox;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static com.curtcox.Check.notNull;

final class Packet {
    public static final Bytes MAGIC = new Bytes("snap".getBytes(StandardCharsets.UTF_8));

    public final String topic;
    public final String message;

    interface Reader {
        Packet read() throws IOException;
    }

    interface Writer {
        void write(Packet packet) throws IOException;
    }

    interface IO extends Reader, Writer {}

    /**
     * Something that accepts things that packets can be written to and from.
     * Networks actively execute methods on the IOs they are given in contrast to the IOs which have no
     * threads or executors.
     */
    interface Network {
        void add(IO io);
    }

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

    Bytes asBytes() {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            OutputStreamPacketWriter writer = new OutputStreamPacketWriter(outputStream);
            writer.write(this);
            return new Bytes(outputStream.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
