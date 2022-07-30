package com.curtcox.snap.model;

import java.io.IOException;
import java.util.*;

import static com.curtcox.snap.util.Check.notNull;

/**
 * An in-memory network.
 */
final class SimpleNetwork implements Packet.Network {

    private final List<Packet.IO> ios = new ArrayList<>();

    private final Runner runner;

    private SimpleNetwork(Runner executor) {
        this.runner = notNull(executor);
    }

    static SimpleNetwork newPolling() {
        return newPolling(Runner.of());
    }

    static SimpleNetwork newPolling(Runner runner) {
        SimpleNetwork network = new SimpleNetwork(runner);
        network.queuePoll();
        return network;
    }

    private void queuePoll() {
        runner.periodically(() -> {
            try {
                exchange();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    synchronized private void exchange() throws IOException {
        Map<Packet.IO,Packet> in = readIncoming();
        for (Packet.IO out : ios) {
            writeOutgoing(in,out);
        }
    }

    private void writeOutgoing(Map<Packet.IO,Packet> ins, Packet.IO out) throws IOException {
        for (Packet packet : combineWithout(ins,out)) {
            if (packet!=null) {
                out.write(packet);
            }
        }
    }

    private List<Packet> combineWithout(Map<Packet.IO,Packet> ins, Packet.IO excluded) {
        List<Packet> packets = new ArrayList<>();
        for (Packet.IO in : ins.keySet()) {
            if (in!=excluded) {
                packets.add(ins.get(in));
            }
        }
        return packets;
    }

    synchronized private Map<Packet.IO,Packet> readIncoming() throws IOException {
        Map<Packet.IO,Packet> in = new HashMap<>();
        for (Packet.IO input : ios) {
            in.put(input,input.read());
        }
        return in;
    }

    synchronized public void add(Packet.IO io) {
        ios.add(io);
    }

}
