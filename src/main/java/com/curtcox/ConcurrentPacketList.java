package com.curtcox;

import java.util.*;

final class ConcurrentPacketList {
    private final List<Packet> list = new ArrayList<>();

    boolean isEmpty() {
        return list.isEmpty();
    }

    void add(Packet packet) {
        list.add(packet);
    }

    Packet removeFirst() {
        return list.remove(0);
    }

    Packet removeFirstMatching(Packet.Filter filter) {
        for (Packet packet : list) {
            if (filter.passes(packet)) {
                list.remove(packet);
                return packet;
            }
        }
        return null;
    }

}
