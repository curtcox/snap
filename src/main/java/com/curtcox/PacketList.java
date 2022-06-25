package com.curtcox;

import java.util.*;

final class PacketList {

    private final List<Packet> list = new ArrayList<>();

    Iterator<Packet> read() {
        return list.iterator();
    }

    Packet read(String topic) {
        for (int i = 0; i< list.size(); i++) {
            Packet packet = list.get(i);
            if (topic.equals(packet.topic)) {
                list.remove(i);
                return packet;
            }
        }
        return null;
    }

    Packet take() {
        return (list.isEmpty()) ? null : list.remove(0);
    }

    void add(Packet packet) {
        list.add(packet);
    }

}
