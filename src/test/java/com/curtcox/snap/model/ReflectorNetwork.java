package com.curtcox.snap.model;

import java.io.IOException;

import static com.curtcox.snap.util.Check.notNull;
import static com.curtcox.snap.model.Packet.*;

/**
 * A network that reflects every packet written back to the writer.
 * This is only useful for writing unit tests.
 */
final class ReflectorNetwork implements Network {

    private final Runner runner;

    ReflectorNetwork(Runner runner) {
        this.runner = notNull(runner);
    }

    @Override
    public void add(IO io) {
        scheduleNextReflection(io);
    }

    private void scheduleNextReflection(final IO io) {
         runner.periodically(() -> reflect(io));
    }

    void reflect(IO io) {
        try {
            Packet packet = io.read(ANY);
            if (packet!=null) {
                io.write(packet);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
