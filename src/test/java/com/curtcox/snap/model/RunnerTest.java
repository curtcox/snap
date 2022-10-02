package com.curtcox.snap.model;

import org.junit.Test;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;

import static org.junit.Assert.*;

public class RunnerTest {

    FakeScheduledExecutorService scheduler = new FakeScheduledExecutorService();
    Clock clock = TestClock.standard;

    Runner runner = new Runner(scheduler,clock);

    @Test
    public void can_create() {
        assertNotNull(new Runner(null,null));
    }

    @Test
    public void stop_shuts_down_scheduler() {
        runner.stop();
        assertTrue(scheduler.shutdown);
    }

    @Test
    public void periodically_calls_runnable() {
        FakeRunnable runnable = new FakeRunnable();
        runner.periodically(runnable);

        assertTrue(runnable.count>0);
    }

    @Test
    public void periodically_calls_callable() {
        FakeCallable runnable = new FakeCallable();
        runner.periodically(runnable);

        assertTrue(runnable.count>0);
    }

    static class FakeRunnable implements Runnable {

        int count;
        @Override
        public void run() {
            count++;
        }
    }

    static class FakeCallable implements Callable {
        int count;
        @Override
        public Object call() throws Exception {
            count++;
            return count;
        }
    }

    static class FakeScheduledExecutorService implements ScheduledExecutorService {

        boolean shutdown;
        @Override public void shutdown() { shutdown=true; }
        @Override public ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit) {
            command.run();
            return null;
        }
// -- Don't do most things.
        @Override public ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) { throw no(); }
        @Override public <V> ScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit) { throw no();}
        @Override public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit) {throw no();}
        @Override public List<Runnable> shutdownNow() { throw no(); }
        @Override public boolean isShutdown() { throw no(); }
        @Override public boolean isTerminated() { throw no(); }
        @Override public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException { throw no(); }
        @Override public <T> Future<T> submit(Callable<T> task) { throw no(); }
        @Override public <T> Future<T> submit(Runnable task, T result) { throw no(); }
        @Override public Future<?> submit(Runnable task) { throw no(); }
        @Override public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException { throw no(); }
        @Override public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException { throw no(); }
        @Override public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException { throw no(); }
        @Override public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException { throw no(); }
        @Override  public void execute(Runnable command) { throw no(); }
    }

    private static UnsupportedOperationException no() {
        return new UnsupportedOperationException();
    }
}
