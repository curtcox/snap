package com.curtcox;

import java.io.IOException;
import java.util.concurrent.*;

import static com.curtcox.Check.notNull;

/**
 * A network that reflects every packet written back to the writer.
 */
final class ReflectorNetwork implements Packet.Network {

    private final ExecutorService executor;

    ReflectorNetwork() {
        this(Executors.newSingleThreadExecutor());
    }

    private ReflectorNetwork(ExecutorService executor) {
        this.executor = notNull(executor);
    }

    @Override
    public void add(Packet.IO io) {
        scheduleNextReflection(io);
    }

    private void scheduleNextReflection(final Packet.IO io) {
         executor.execute(new Runnable() {
             @Override
             public void run() {
                 reflect(io);
                 scheduleNextReflection(io);
             }
         });
    }

    void reflect(Packet.IO io) {
        try {
            Packet packet = io.read();
            if (packet!=null) {
                io.write(packet);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    void shutdown() {
        executor.shutdown();
    }
}
