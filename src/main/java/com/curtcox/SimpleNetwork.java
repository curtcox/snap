package com.curtcox;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;

import static com.curtcox.Check.notNull;

final class SimpleNetwork implements Packet.Network {

    private final List<Packet.IO> ios = new ArrayList<>();

    private final ExecutorService executor;

    private SimpleNetwork(ExecutorService executor) {
        this.executor = notNull(executor);
    }

    private SimpleNetwork() {
        this(Executors.newSingleThreadExecutor());
    }

    static SimpleNetwork newPolling() {
        SimpleNetwork network = new SimpleNetwork();
        network.queuePoll();
        return network;
    }

    private void queuePoll() {
        executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            exchange();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        queuePoll();
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
