package com.curtcox.snap.model;

public final class Clock {

    static final long tick = 10;

    public static void tick() {
        tick(1);
    }

    public static void tick(int count) {
        try {
            Thread.sleep(tick *  count);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
