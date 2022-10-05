package com.curtcox.snap.model;

import java.util.*;
import static com.curtcox.snap.model.Packet.*;
/**
 * A mutable list of packets that can be accessed by multiple threads concurrently.
 */
// TODO FIXME Probably should combine and rename SinkReader/ConcurrentPacketList into new PacketList class that implements or exposes a List.
public final class ConcurrentPacketList
        implements Reader, Sink
{
    private final List<Packet> list = new ArrayList<>();

    synchronized boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    synchronized public boolean add(Packet packet) {
        return list.add(packet);
    }

    @Override
    public synchronized Packet read(Filter filter) {
        for (Packet packet : list) {
            if (filter.passes(packet)) {
                list.remove(packet);
                return packet;
            }
        }
        return null;
    }

    @Override
    public synchronized String toString() {
        return "Concurrent packets " + list;
    }
}
