package com.curtcox;

import java.util.ArrayList;
import java.util.List;

public final class Snap {

    List<Packet> packets = new ArrayList<>();

    public void send(String topic, String message) {
        packets.add(new Packet(topic,message));
    }

    public Packet listen(String topic) {
        return listen();
    }

    public Packet listen() {
        return packets.remove(0);
    }

}
