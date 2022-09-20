package com.curtcox.snap.model;

/**
 * For globally doing things faster or slower.
 * This can be useful for testing and debugging.
 */
public final class Clock {

    public static final Clock standard = new Clock(10);

    public Clock(long tick) {
        this.tick = tick;
    }

    public final long tick;

    public void tick() {
        tick(1);
    }

    public void tick(int count) {
        try {
            Thread.sleep(tick *  count);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
