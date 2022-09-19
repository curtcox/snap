package com.curtcox.snap.model;

import java.util.concurrent.*;

import static com.curtcox.snap.model.Clock.tick;

/**
 * For periodically running a Runnable on an ScheduledExecutorService until it shuts down.
 */
public final class Runner {

    private final ScheduledExecutorService scheduler;

    Runner(ScheduledExecutorService scheduler) {
        this.scheduler = scheduler;
    }

    public static Runner of() {
        return new Runner(Executors.newScheduledThreadPool(1));
    }

    public void periodically(Runnable command) {
        scheduler.scheduleAtFixedRate(command,tick,tick,TimeUnit.MILLISECONDS);
    }

    public void stop() {
        scheduler.shutdown();
    }

}
