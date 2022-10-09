package com.curtcox.snap.model;

import java.util.*;
import java.util.concurrent.*;

public final class TestRunner {

    static final boolean debug = Debug.on;

    public static Runner once() {
        return new Runner(new TimesExecutor(),TestClock.standard);
    }


static class TimesExecutor implements ScheduledExecutorService {

    @Override public ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit) {
        if (debug) debug("running " + command);
        command.run();
        if (debug) debug("ran " + command);
        return null;
    }

    // --------------------------- never ---------------------------
    @Override public ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) { throw no(); }
    @Override public <V> ScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit) { throw no(); }
    @Override public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit) {throw no();}
    @Override public void shutdown() { throw no(); }
    @Override public List<Runnable> shutdownNow() { throw no(); }
    @Override public boolean isShutdown() { throw no();}
    @Override public boolean isTerminated() { throw no();}
    @Override public boolean awaitTermination(long timeout, TimeUnit unit) { throw no(); }
    @Override public <T> Future<T> submit(Callable<T> task) { throw no(); }
    @Override public <T> Future<T> submit(Runnable task, T result) { throw no(); }
    @Override public Future<?> submit(Runnable task) { throw no(); }
    @Override public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) { throw no(); }
    @Override public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) { throw no(); }
    @Override public <T> T invokeAny(Collection<? extends Callable<T>> tasks) { throw no(); }
    @Override public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) {throw no();}
    @Override public void execute(Runnable command) { throw no(); }
}

    private static UnsupportedOperationException no() {
        return new UnsupportedOperationException("Implement if needed.");
    }

    private static void debug(String message) {
        System.out.println("TestRunner " + message);
    }

}
