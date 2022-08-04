package com.curtcox.snap.model;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static com.curtcox.snap.util.Check.notNull;

/**
 * An immutable bundle of data that can be sent over networks from place to place.
 * This file contains low-level interfaces for dealing with packets.
 * Packets are hostile to comparison to other classes.
 */
public final class Packet {
    public static final Bytes MAGIC = new Bytes("snap".getBytes(StandardCharsets.UTF_8));
    /**
     * See https://en.wikipedia.org/wiki/Maximum_transmission_unit
     */
    public static final int MAX_SIZE = 2304;

    public static final Filter ANY = packet -> true;

    public final String topic;
    public final String message;
    public final String sender;
    public final long timestamp;
    public final long trigger;

    /**
     * For reading one or more packets.
     */
    public interface Reader {
        /**
         * Immediately return a packet or null if there is none.
         */
        Packet read(Filter filter) throws IOException;
    }

    /**
     * For writing one or more packets.
     */
    public interface Writer {
        void write(Packet packet) throws IOException;
    }

    public interface IO extends Reader, Writer {}

    /**
     * Something that accepts things that packets can be written to and from.
     * Networks actively execute methods on the IOs they are given in contrast to the IOs which have no
     * threads or executors.
     */
    public interface Network {

        enum Type {
            /**
             * Just within the memory of this JVM.
             */
            memory
        }
        /**
         * Add an IO to this network.
         * The IO methods will be invoked by network threads.
         */
        void add(IO io);
    }

    /**
     * For filtering packets.
     */
    interface Filter {
        boolean passes(Packet packet);
    }

    public Packet(String sender,String topic, String message) {
        this(sender,topic,message,now(),0);
    }

    public Packet(String sender,String topic, String message, long timestamp, long trigger) {
        this.timestamp = timestamp;
        this.trigger = trigger;
        this.sender = notNull(sender);
        this.topic = notNull(topic);
        this.message = notNull(message);
        checkSize();
    }

    private void checkSize() {
        int size = asBytes().length;
        if (size > MAX_SIZE) {
            throw new IllegalArgumentException("Packet would be too big. " + size + " > " + MAX_SIZE);
        }
    }

    private static long now() {
        return System.currentTimeMillis();
    }

    public boolean equals(Object o) {
        Packet that  = (Packet) o;
        return topic.equals(that.topic) && message.equals(that.message) && sender.equals(that.sender) &&
                timestamp == that.timestamp && trigger==that.trigger;
    }

    public int hashCode() {
        return topic.hashCode() ^ message.hashCode() ^ sender.hashCode() ^ Long.hashCode(timestamp) ^ Long.hashCode(trigger);
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
