package com.curtcox;

import java.io.IOException;

import static com.curtcox.Check.notNull;

/**
 * A network that reflects every packet written back to the writer.
 */
final class ReflectorNetwork implements Packet.Network {

    private final Runner runner;

    ReflectorNetwork() {
        this(Runner.of());
    }

    private ReflectorNetwork(Runner runner) {
        this.runner = notNull(runner);
    }

    @Override
    public void add(Packet.IO io) {
        scheduleNextReflection(io);
    }

    private void scheduleNextReflection(final Packet.IO io) {
         runner.periodically(() -> reflect(io));
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

}
