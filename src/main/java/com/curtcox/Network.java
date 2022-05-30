package com.curtcox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

final class Network {

    private final List<Node> nodes = new ArrayList<>();
    private final List<Packet.IO> ios = new ArrayList<>();

    void send(Packet packet) {
        for (Node node : nodes) {
            node.receive(packet);
        }
        for (Packet.IO io: ios) {
            try {
                io.write(packet);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    void add(Node receiver) {
        nodes.add(receiver);
    }

    void add(Packet.IO io) {
        ios.add(io);
        try {
            Packet packet = io.read();
            if (packet!=null) {
                send(packet);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
