package com.curtcox.snap.model;

import java.util.*;

/**
 * A mutable list of packets that can be accessed by multiple threads concurrently.
 */
final class ConcurrentPacketList {
    private final List<Packet> list = new ArrayList<>();

    synchronized boolean isEmpty() {
        return list.isEmpty();
    }

    synchronized void add(Packet packet) {
        list.add(packet);
    }

    synchronized Packet removeFirst() {
        return list.remove(0);
    }

    synchronized Packet removeFirstMatching(Packet.Filter filter) {
        for (Packet packet : list) {
            if (filter.passes(packet)) {
                list.remove(packet);
                return packet;
            }
        }
        return null;
    }

}
