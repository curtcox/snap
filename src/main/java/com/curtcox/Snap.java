package com.curtcox;

import java.util.ArrayList;
import java.util.List;

import static com.curtcox.Check.notNull;

public final class Snap {

    private final Network network;
    private final List<Packet> packets = new ArrayList<>();

    public Snap(Network network) {
        this.network = notNull(network);
    }

    public static Snap on(Network network) {
        Snap snap = new Snap(network);
        network.add(snap);
        return snap;
    }

    public void send(String topic, String message) {
        Packet packet = new Packet(topic,message);
        receive(packet);
        network.send(packet);
    }

    void receive(Packet packet) {
        packets.add(packet);
    }

    public Packet listen(String topic) {
        for (int i=0; i< packets.size(); i++) {
            Packet packet = packets.get(i);
            if (topic.equals(packet.topic)) {
                packets.remove(i);
                return packet;
            }
        }
        return null;
    }

    public Packet listen() {
        return (packets.isEmpty()) ? null : packets.remove(0);
    }

}
