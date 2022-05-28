package com.curtcox;

import java.util.ArrayList;
import java.util.List;

final class Network {

    private final List<Packet.Receiver> receivers = new ArrayList<>();

    public void send(Packet packet) {
        for (Packet.Receiver snap : receivers) {
            snap.receive(packet);
        }
    }

    public void add(Packet.Receiver snap) {
        receivers.add(snap);
    }
}
