package com.curtcox.snap.model;

import java.io.IOException;
import java.util.*;

import static com.curtcox.snap.util.Check.notNull;
import static com.curtcox.snap.model.Packet.*;

/**
 * An in-memory network.
 */
public final class SimpleNetwork implements Network {

    private static final boolean debug = Debug.on;
    private final List<IO> ios = new ArrayList<>();
    private final Map<Packet,IO> sources = new HashMap<>();

    private final Runner runner;

    private SimpleNetwork(Runner runner) {
        this.runner = notNull(runner);
    }

    public static SimpleNetwork newPolling() {
        return newPolling(Runner.of());
    }

    public static SimpleNetwork newPolling(Runner runner) {
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
            if (packet!=null && !sources.get(packet).equals(out)) {
                write(out,packet);
            }
        }
    }

    /**
     * So that packets aren't sent right back to their sources.
     */
    private List<Packet> combineWithout(Map<IO,Packet> ins, IO excluded) {
        List<Packet> packets = new ArrayList<>();
        for (IO in : ins.keySet()) {
            if (in!=excluded) {
                packets.add(ins.get(in));
            }
        }
        return packets;
    }

    synchronized private Map<IO,Packet> readIncoming() throws IOException {
        Map<IO,Packet> in = new HashMap<>();
        for (IO input : ios) {
            Packet packet = read(input);
            if (packet!=null) {
                in.put(input,packet);
                if (!sources.containsKey(packet)) {
                    sources.put(packet,input);
                }
            }
        }
        return in;
    }

    private static Packet read(IO input) throws IOException {
        Packet packet = input.read(ANY);
        if (debug) {
            System.out.println("Network read " + packet + " from " + input);
        }
        return packet;
    }

    private static void write(IO out, Packet packet) throws IOException {
        if (debug) {
            System.out.println("Network writing " + packet + " to " + out);
        }
        out.write(packet);
    }

    synchronized public void add(IO io) {
        ios.add(io);
    }

}
