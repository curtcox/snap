package com.curtcox;

import java.util.ArrayList;
import java.util.List;

final class Network {

    private final List<Node> nodes = new ArrayList<>();

    void send(Packet packet) {
        for (Node node : nodes) {
            node.receive(packet);
        }
    }

    void add(Node receiver) {
        nodes.add(receiver);
    }

    void add(Packet.IO io) {
    }

}
