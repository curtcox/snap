package com.curtcox;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.curtcox.Check.notNull;

final class Node {

    private final List<Packet> fromNetwork = packets();
    private final List<Packet> fromOther = packets();

    private static List<Packet> packets() {
        return Collections.synchronizedList(new ArrayList<Packet>());
    }

    static Node on(Packet.Network network) {
        final Node node = new Node();
        network.add(node.newIO());
        return node;
    }

    private Packet.IO newIO() {
        return new Packet.IO() {
            @Override public Packet read() { return take(); }
            @Override public void write(Packet packet) { receive(packet);}
        };
    }

    private void receive(Packet packet) {
        fromNetwork.add(notNull(packet));
    }

    void write(Packet packet) {
        fromOther.add(notNull(packet));
    }

    Packet read(String topic) {
        for (int i = 0; i< fromNetwork.size(); i++) {
            Packet packet = fromNetwork.get(i);
            if (topic.equals(packet.topic)) {
                fromNetwork.remove(i);
                return packet;
            }
        }
        return null;
    }

    Packet read() {
        return (fromNetwork.isEmpty()) ? null : fromNetwork.remove(0);
    }
    Packet take() {
        return (fromOther.isEmpty()) ? null : fromOther.remove(0);
    }

}
