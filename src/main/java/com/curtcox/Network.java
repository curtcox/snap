package com.curtcox;

import java.io.IOException;
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
        try {
            Packet packet = io.read();
            if (packet!=null) {
                io.write(packet);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
