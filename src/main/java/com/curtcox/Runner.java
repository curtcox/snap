package com.curtcox;

import java.util.concurrent.*;

/**
 * For periodically running a Runnable on an ExecutorService until it shuts down.
 */
final class Runner {

    private final ExecutorService executorService;

    Runner(ExecutorService executorService) {
        this.executorService = executorService;
    }

    public static Runner of() {
        return new Runner(Executors.newSingleThreadExecutor());
    }

    void periodically(Runnable command) {
        executeAndReschedule(command);
    }

    private void executeAndReschedule(final Runnable command) {
        if (!executorService.isShutdown()) {
            executorService.execute(command);
            executorService.execute(() -> executeAndReschedule(command));
        }
    }


}
