package com.curtcox.snap.model;

public final class Random {

    public static String random(String prefix) {
        return prefix + " " + System.nanoTime() % 1000;
    }

    static Packet.Sender sender() {
        return new Packet.Sender(random("sender"));
    }

    static Packet.Topic topic() {
        return new Packet.Topic(random("topic"));
    }

    static Packet packet() {
        return Packet.builder()
                .sender(sender())
                .topic(topic())
                .message(random("message"))
                .build();
    }
}
