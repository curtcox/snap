package com.curtcox;

import java.util.ArrayList;
import java.util.List;

import static com.curtcox.Check.notNull;

final class Node {

    private final Network network;
    private final List<Packet> packets = new ArrayList<>();

    private Node(Network network) {
        this.network = notNull(network);
    }

    static Node on(Network network) {
        Node node = new Node(network);
        network.add(node);
        return node;
    }

    public void send(String topic, String message) {
        network.send(new Packet(topic,message));
    }

    void receive(Packet packet) {
        packets.add(packet);
    }

    Packet listen(String topic) {
        for (int i=0; i< packets.size(); i++) {
            Packet packet = packets.get(i);
            if (topic.equals(packet.topic)) {
                packets.remove(i);
                return packet;
            }
        }
        return null;
    }

    Packet listen() {
        return (packets.isEmpty()) ? null : packets.remove(0);
    }

}
