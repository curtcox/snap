package com.curtcox;

import java.util.ArrayList;
import java.util.List;

final class Network {

    private final List<Snap> snaps = new ArrayList<>();

    public void send(Packet packet) {
        for (Snap snap : snaps) {
            snap.receive(packet);
        }
    }

    public void add(Snap snap) {
        snaps.add(snap);
    }
}
