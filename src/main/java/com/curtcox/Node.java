package com.curtcox;

import java.util.*;

import static com.curtcox.Check.notNull;

final class Node {

    private final PacketList fromNetwork = new PacketList();
    private final PacketList fromOther = new PacketList();

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

    private void receive(Packet packet) { fromNetwork.add(packet); }

    void write(Packet packet) {
        fromOther.add(notNull(packet));
    }

    Packet read(String topic) {
        return fromNetwork.read(topic);
    }

    Iterator<Packet> read() {
        return fromNetwork.read();
    }
    Packet take() {
        return fromOther.take();
    }

}
