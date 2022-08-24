package com.curtcox.snap.model;

import static com.curtcox.snap.util.Check.notNull;
import com.curtcox.snap.model.Packet.*;
/**
 * NodeS exist to transfer PacketS between a Network and something else.
 * A node will normally be used by at least two different threads.
 * NodeS are stateless and delegate state logic to PacketListS.
 */
final class Node implements Reader.Factory {

    private final PacketReaderFactory fromNetwork = new PacketReaderFactory();
    private final PacketReaderFactory fromOther = new PacketReaderFactory();

    static Node on(Network network) {
        final Node node = new Node();
        network.add(node.networkIO());
        return node;
    }

    private IO networkIO() {
        // Methods in this IO are invoked by network threads.
        return new IO() {
            @Override public Packet read(Filter filter) {
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

    @Override
    public Reader reader(Filter filter) {
        return fromNetwork.reader(filter);
    }

}
