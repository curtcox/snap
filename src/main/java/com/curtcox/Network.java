package com.curtcox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static com.curtcox.Check.notNull;

final class Network {

    private final List<Node> nodes = new ArrayList<>();
    private final List<Packet.IO> ios = new ArrayList<>();

    private final Executor executor;

    private Network(Executor executor) {
        this.executor = notNull(executor);
    }

    private Network() {
        this(Executors.newSingleThreadExecutor());
    }

    static Network newPolling() {
        Network network = new Network();
        network.queuePoll();
        return network;
    }

    private void queuePoll() {
        executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        poll();
                        queuePoll();
                    }
                });
    }

    private void poll() {
        for (Packet.IO io : ios) {
            poll(io);
        }
    }

    private void poll(Packet.IO io) {
        try {
            Packet packet = io.read();
            if (packet!=null) {
                send(packet);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    void send(Packet packet) {
        for (Node node : nodes) {
            node.receive(packet);
        }
        for (Packet.IO io: ios) {
            try {
                io.write(packet);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    void add(Node receiver) {
        nodes.add(receiver);
    }

    void add(Packet.IO io) {
        ios.add(io);
    }

}
