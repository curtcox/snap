package com.curtcox.snap.model;

import java.io.IOException;
import java.util.*;

import static com.curtcox.snap.util.Check.notNull;
import static com.curtcox.snap.model.Packet.*;

/**
 * An in-memory network.
 */
public final class SimpleNetwork implements Network {

    private final List<Packet.IO> ios = new ArrayList<>();

    private final Runner runner;

    private SimpleNetwork(Runner runner) {
        this.runner = notNull(runner);
    }

    public static SimpleNetwork newPolling() {
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
        Map<IO,Packet> in = readIncoming();
        for (IO out : ios) {
            writeOutgoing(in,out);
        }
    }

    private void writeOutgoing(Map<IO,Packet> ins, IO out) throws IOException {
        for (Packet packet : combineWithout(ins,out)) {
            if (packet!=null) {
                out.write(packet);
            }
        }
    }

    private List<Packet> combineWithout(Map<IO,Packet> ins, IO excluded) {
        List<Packet> packets = new ArrayList<>();
        for (IO in : ins.keySet()) {
            if (in!=excluded) {
                packets.add(ins.get(in));
            }
        }
        return packets;
    }

    synchronized private Map<Packet.IO,Packet> readIncoming() throws IOException {
        Map<IO,Packet> in = new HashMap<>();
        for (IO input : ios) {
            in.put(input,input.read(Packet.ANY));
        }
        return in;
    }

    synchronized public void add(IO io) {
        ios.add(io);
    }

}
