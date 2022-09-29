package com.curtcox.snap.model;

import static com.curtcox.snap.util.Check.notNull;
import com.curtcox.snap.model.Packet.*;

import java.io.IOException;

/**
 * NodeS exist to transfer PacketS between a Network and something else.
 * A node will normally be used by at least two different threads.
 */
final class Node implements Reader.Factory, Sink.Acceptor, IO {
    // We are stateless to the extent that they delegate state logic to these.
    private final PacketReaderFactory fromNetwork = new PacketReaderFactory(); // stores packets from the network
    private final PacketReaderFactory fromOther = new PacketReaderFactory(); // stores packets to the network
    private final CombinedSink sinks = new CombinedSink(); // the sink where we send packets from the network

    /**
     * Create a node on the given network.
     */
    static Node on(Network network) {
        final Node node = new Node();
        network.add(node.networkIO());
        return node;
    }

    public static void bridge(Network network1, Network network2) {
        final Node node = new Node();
        network1.add(node.networkIO());
        network2.add(node);
    }

    private IO networkIO() {
        // Methods in this IO are invoked by network threads.
        return new IO() {
            @Override public Packet read(Filter filter) {
                return fromOther.take();
            }

            final UniquePacketFilter filter = new UniquePacketFilter();
            @Override public void write(Packet packet) {
                if (filter.passes(packet)) {
                    fromNetwork.add(packet);
                    sinks.add(packet);
                } else {
                    System.out.println("Discarded " + packet);
                }
            }
        };
    }

    // These methods are invoked by threads other than those in the networkIO above
    @Override
    public void write(Packet packet) {
        fromOther.add(notNull(packet));
    }

    @Override
    public Reader reader(Filter filter) {
        return fromNetwork.reader(filter);
    }

    @Override
    public void on(Sink sink) {
        sinks.add(sink);
    }

    @Override
    public Packet read(Filter filter) throws IOException {
        return fromNetwork.reader(filter).read(filter);
    }
}
