package com.curtcox;

import java.util.ArrayList;
import java.util.List;

public final class Snap {

    List<Packet> packets = new ArrayList<>();

    public void send(String topic, String message) {
        packets.add(new Packet(topic,message));
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
