package com.curtcox.snap.model;

import java.util.*;

final class FakeIO implements Packet.IO {

    private final List<Packet> toRead = new ArrayList<>();
    private final List<Packet> written = new ArrayList<>();
    @Override
    synchronized public Packet read(Packet.Filter filter) {
        if (toRead.isEmpty()) {
            return null;
        }
        return toRead.remove(0);
    }

    @Override
    synchronized public void write(Packet packet) {
        written.add(packet);
    }

    synchronized void add(Packet packet) {
        toRead.add(packet);
    }

    synchronized public List<Packet> written() {
        return new ArrayList<>(written);
    }
}
