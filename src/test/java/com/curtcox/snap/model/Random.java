package com.curtcox.snap.model;

import com.curtcox.snap.model.Packet.*;

public final class Random {

    private static long counter;

    public static String random(String prefix) {
        counter++;
        return prefix + " " + Long.toHexString(counter) + Long.toHexString(System.nanoTime() & 0xffff);
    }

    static Sender sender() {
        return new Sender(random("sender"));
    }

    public static Topic topic() {
        return new Topic(random("topic"));
    }

    public static Packet packet() {
        return Packet.builder()
                .sender(sender())
                .topic(topic())
                .message(random("message"))
                .build();
    }
}
