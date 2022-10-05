package com.curtcox.snap.model;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

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

    public final Topic topic;
    public final String message;
    public final Sender sender;
    public final Timestamp timestamp;
    public final Trigger trigger;

    public static final class Timestamp {
        public final long value;

        public Timestamp(long value) {
            this.value = value;
        }

        @Override
        public int hashCode() {
            return Long.hashCode(value);
        }

        @Override
        public boolean equals(Object o) {
             Timestamp that = (Timestamp) o;
             return value == that.value;
        }
        @Override public String toString() { return Long.toHexString(value); }

        public static Timestamp now() {
            return new Timestamp(System.currentTimeMillis());
        }

    }

    /**
     * A packet and when it was received.
     * TODO FIXME This should probably be Event instead of Receipt and support types of Sent/Received/Created
     */
    public static class Receipt {

        public final Packet packet;
        public final Timestamp received;

        public Receipt(Packet packet, Timestamp received) {
            this.packet = packet;
            this.received = received;
        }

        public Receipt(Packet packet) {
            this(packet,Timestamp.now());
        }

        @Override
        public String toString() {
            return packet + " at " + received;
        }
    }
    /**
     * Typesafe string for the name of group of packets about the same thing.
     * Topic as in topic of conversation. What these packets are about.
     */
    public static final class Topic {
        final String value;
        public Topic(String value) {
            this.value = value;
        }
        @Override public String toString() { return value; }
        @Override public int hashCode() { return value.hashCode(); }
        @Override public boolean equals(Object o) {
            Topic that = (Topic) o;
            return value.equals(that.value);
        }

        public boolean matches(Topic.Spec b) {
            return b.matches(this);
        }

        /**
         * A spec that matches some topics and not others.
         */
        public static final class Spec {
            public final String value;

            public Spec(String value) {
                this.value = value;
            }

            public Spec(Topic topic) {
                this(topic.value);
            }

            public boolean matches(Topic b) {
                return value.isEmpty() || Arrays.asList(b.value.split(" ")).contains(value);
            }
            @Override public String toString() { return value; }
        }
    }

    /**
     * A sink guarded by a filter.
     */
    public static final class FilteredSink implements Sink {
        final Filter filter;
        final Sink sink;

        public FilteredSink(Filter filter, Sink sink) {
            this.filter = filter;
            this.sink = sink;
        }

        @Override
        public boolean add(Packet packet) {
            if (filter.passes(packet)) {
                sink.add(packet);
                return true;
            }
            return false;
        }
    }

    /**
     * The name of who sent a packet.
     */
    public static final class Sender {
        final String value;
        Sender(String value) {
            this.value = value;
        }

        @Override public String toString() { return value; }
        @Override public int hashCode() { return value.hashCode(); }
        @Override public boolean equals(Object o) {
            Sender that = (Sender) o;
            return value.equals(that.value);
        }
    }

    /**
     * The trigger or cause of a packet or NONE if there isn't any.
     * For example, a reply packet would have a request packet as a trigger.
     */
    public static final class Trigger {

        private final long value;

        public static final Trigger NONE = new Trigger(0);

        private Trigger(long value) {
            this.value = value;
        }

        public static Trigger from(Packet packet) {
            return new Trigger(packet.hashCode());
        }

        public static Trigger from(long value) {
            return new Trigger(value);
        }

        public long toLong() {
            return value;
        }

        @Override
        public int hashCode() {
            return (int) value;
        }

        @Override
        public boolean equals(Object o) {
            Trigger that = (Trigger) o;
            return value == that.value;
        }

        @Override public String toString() { return Long.toHexString(value); }

        public boolean refersTo(Packet packet) {
            return value == packet.hashCode();
        }
    }

    /**
     * For reading one or more packets.
     */
    public interface Reader {
        /**
         * Immediately return a packet or null if there is none.
         * Reading a packet may prevent others from reading it, so clients should generally use filters to only read
         * what they are interested in processing.
         */
        Packet read(Filter filter) throws IOException;

        interface Factory {
            Reader reader(Filter readerFilter);
        }
    }

    /**
     * Something that accepts packets -- although not necessarily all packets.
     * Perhaps this should be the same interface as Writer.
     * FIXME TODO Should this also be a Consumer of Packet
     * https://docs.oracle.com/javase/8/docs/api/java/util/function/Consumer.html
     */
    public interface Sink {
        /**
         * Return true if and only if the packet was accepted.
         */
        boolean add(Packet packet);

        /**
         * Something that accepts a packet sink.
         */
        interface Acceptor {
            void on(Sink sink);
        }
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
    public interface Filter {
        boolean passes(Packet packet);
    }

    public Packet(Sender sender,Topic topic, String message) {
        this(sender,topic,message,now(),Trigger.NONE);
    }

    public Packet(Sender sender,Topic topic, String message, Timestamp timestamp, Trigger trigger) {
        this.timestamp = timestamp == null ? now() : timestamp;
        this.trigger = trigger == null ? Trigger.NONE : trigger;
        this.sender = notNull(sender);
        this.topic = notNull(topic);
        this.message = notNull(message);
        checkSize();
    }

    static Builder builder() {
        return new Builder();
    }

    static class Builder {
        private Sender sender;
        private Topic topic;
        private String message;
        private Trigger trigger;
        private Timestamp timestamp;
        Builder sender(Sender sender) {
            this.sender = sender;
            return this;
        }
        Builder topic(Topic topic) {
            this.topic = topic;
            return this;
        }
        Builder message(String message) {
            this.message = message;
            return this;
        }
        Builder timestamp(Timestamp timestamp) {
            this.timestamp = timestamp;
            return this;
        }
        Builder trigger(Trigger trigger) {
            this.trigger = trigger;
            return this;
        }
        Packet build() {
            return new Packet(sender,topic,message,timestamp,trigger);
        }
    }

    private void checkSize() {
        int size = asBytes().length;
        if (size > MAX_SIZE) {
            throw new IllegalArgumentException("Packet would be too big. " + size + " > " + MAX_SIZE);
        }
    }

    private static Timestamp now() {
        return Timestamp.now();
    }

    public boolean equals(Object o) {
        Packet that  = (Packet) o;
        return topic.equals(that.topic) && message.equals(that.message) && sender.equals(that.sender) &&
                timestamp.equals(that.timestamp) && trigger.equals(that.trigger);
    }

    public int hashCode() {
        return topic.hashCode() ^ message.hashCode() ^ sender.hashCode() ^ timestamp.hashCode() ^ trigger.hashCode();
    }

    @Override
    public String toString() {
       return timestamp + " " + sender + " " + topic + " " + message + " " + trigger;
    }

    public Bytes asBytes() {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            OutputStreamPacketWriter writer = new OutputStreamPacketWriter(outputStream);
            writer.write(this);
            return new Bytes(outputStream.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Packet from(Bytes bytes) throws IOException {
        if (!bytes.startsWith(Packet.MAGIC)) {
            throw new IOException("Snap packet expected");
        }
        byte[] raw = bytes.value();
        int skip1 = Packet.MAGIC.length;
        long timestamp = longAt(raw,skip1);
        int skip2 = skip1 + Long.BYTES;
        Packet.Trigger trigger = Packet.Trigger.from(longAt(raw,skip2));
        int skip3 = skip2 + Long.BYTES;
        String sender = stringAt(raw,skip3);
        int skip4 = skip3 + 2 + sender.length();
        String topic = stringAt(raw,skip4);
        int skip5 = skip4 + 2 + topic.length();
        String message = stringAt(raw,skip5);
        return Packet.builder()
                .sender(new Packet.Sender(sender))
                .topic(new Packet.Topic(topic))
                .message(message)
                .timestamp(new Packet.Timestamp(timestamp)).trigger(trigger)
                .build();
    }

    private static long longAt(final byte[] b, int offset) {
        long result = 0;
        for (int i = 0; i < Long.BYTES; i++) {
            result <<= Byte.SIZE;
            result |= (b[i + offset] & 0xFF);
        }
        return result;
    }

    private static String stringAt(byte[] raw, int offset) {
        int hi = raw[offset]     & 0xFF;
        int lo = raw[offset + 1] & 0xFF;
        int length = hi * 255 + lo;
        return new String(raw, offset + 2, length, StandardCharsets.UTF_8);
    }

}
