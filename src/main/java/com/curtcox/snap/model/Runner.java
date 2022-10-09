package com.curtcox.snap.model;

import java.util.concurrent.*;

/**
 * For periodically running a Runnable on an ScheduledExecutorService until it shuts down.
 */
public final class Runner {

    private final ScheduledExecutorService scheduler;
    private final Clock clock;

    public Runner(ScheduledExecutorService scheduler, Clock clock) {
        this.scheduler = scheduler;
        this.clock = clock;
    }

    public static Runner of() {
        return new Runner(Executors.newScheduledThreadPool(1), Clock.standard);
    }

    public void periodically(Runnable command) {
        scheduler.scheduleAtFixedRate(command,clock.tick,clock.tick,TimeUnit.MILLISECONDS);
    }

    public void periodically(Callable command) {
        scheduler.scheduleAtFixedRate(new FutureTask<>(command),clock.tick,clock.tick,TimeUnit.MILLISECONDS);
    }

    public void stop() {
        scheduler.shutdown();
    }

}
