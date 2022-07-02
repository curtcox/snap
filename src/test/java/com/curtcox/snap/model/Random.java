package com.curtcox.snap.model;

final class Random {

    static String random(String prefix) {
        return prefix + " " + System.nanoTime() % 1000;
    }

    static Packet packet() {
        return new Packet(random("topic"),random("message"));
    }
}
