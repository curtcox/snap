package com.curtcox.snap.model;

import java.util.*;

/**
 * A mutable list of packets that can be accessed by multiple threads concurrently.
 */
final class ConcurrentPacketList implements Packet.Reader {
    private final List<Packet> list = new ArrayList<>();

    synchronized boolean isEmpty() {
        return list.isEmpty();
    }

    synchronized void add(Packet packet) {
        list.add(packet);
    }

    @Override
    public synchronized Packet read(Packet.Filter filter) {
        for (Packet packet : list) {
            if (filter.passes(packet)) {
                list.remove(packet);
                return packet;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "Concurrent packets " + list;
    }
}
