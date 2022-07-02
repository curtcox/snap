package com.curtcox.snap.model;

public final class Random {

    public static String random(String prefix) {
        return prefix + " " + System.nanoTime() % 1000;
    }

    static Packet packet() {
        return new Packet(random("sender"),random("topic"),random("message"));
    }
}
