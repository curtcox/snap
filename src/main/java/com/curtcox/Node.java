package com.curtcox;

import java.util.*;

import static com.curtcox.Check.notNull;

/**
 * NodeS exist to transfer PacketS between a Network and something else.
 * A node will normally be used by at least two different threads.
 * NodeS are stateless and delegate state logic to PacketListS.
 */
final class Node {

    private final PacketList fromNetwork = new PacketList();
    private final PacketList fromOther = new PacketList();

    static Node on(Packet.Network network) {
        final Node node = new Node();
        network.add(node.networkIO());
        return node;
    }

    private Packet.IO networkIO() {
        // Methods in this IO are invoked by network threads.
        return new Packet.IO() {
            @Override public Packet read() {
                return fromOther.take();
            }
            @Override public void write(Packet packet) {
                fromNetwork.add(packet);
            }
        };
    }

    // These methods are invoked by threads other than those in the networkIO above
    void write(Packet packet) {
        fromOther.add(notNull(packet));
    }

    Iterator<Packet> read(String topic) {
        return fromNetwork.read(topic);
    }

    Iterator<Packet> read() {
        return fromNetwork.read();
    }

}
