package com.curtcox.snap.model;

import com.curtcox.snap.model.Packet.*;

public final class Random {

    public static String random(String prefix) {
        return prefix + " " + System.nanoTime() % 1000;
    }

    static Sender sender() {
        return new Sender(random("sender"));
    }

    public static Topic topic() {
        return new Topic(random("topic"));
    }

    static Packet packet() {
        return Packet.builder()
                .sender(sender())
                .topic(topic())
                .message(random("message"))
                .build();
    }
}
