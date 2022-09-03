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

    public final Topic topic;
    public final String message;
    public final Sender sender;
    public final long timestamp;
    public final Trigger trigger;

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

        public boolean matches(Topic b) {
            return equals(b);
        }
    }

    static final class TopicSink implements Sink {
        final Topic topic;
        final Sink sink;

        TopicSink(Topic topic, Sink sink) {
            this.topic = topic;
            this.sink = sink;
        }

        @Override
        public boolean add(Packet packet) {
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
    }

    /**
     * For reading one or more packets.
     */
    public interface Reader {
        /**
         * Immediately return a packet or null if there is none.
         * Reading a packet prevents others from reading it, so clients should generally use filters to only read
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
     */
    public interface Sink {
        /**
         * Return true if and only if the packet was accepted.
         */
        boolean add(Packet packet);
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
    interface Filter {
        boolean passes(Packet packet);
    }

    public Packet(Sender sender,Topic topic, String message) {
        this(sender,topic,message,now(),Trigger.NONE);
    }

    public Packet(Sender sender,Topic topic, String message, Long timestamp, Trigger trigger) {
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
        private Long timestamp;
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
        Builder timestamp(Long timestamp) {
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

    private static long now() {
        return System.currentTimeMillis();
    }

    public boolean equals(Object o) {
        Packet that  = (Packet) o;
        return topic.equals(that.topic) && message.equals(that.message) && sender.equals(that.sender) &&
                timestamp == that.timestamp && trigger.equals(that.trigger);
    }

    public int hashCode() {
        return topic.hashCode() ^ message.hashCode() ^ sender.hashCode() ^ Long.hashCode(timestamp) ^ trigger.hashCode();
    }

    @Override
    public String toString() {
       return timestamp + " " + sender + " " + topic + " " + message + " " + trigger;
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
