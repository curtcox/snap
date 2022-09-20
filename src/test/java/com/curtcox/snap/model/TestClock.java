package com.curtcox.snap.model;

public final class TestClock {

    public static Clock standard = new Clock(Clock.standard.tick * 5);

    public static void tick() {
        standard.tick();
    }

    public static void tick(int count) {
        standard.tick(count);
    }

}
