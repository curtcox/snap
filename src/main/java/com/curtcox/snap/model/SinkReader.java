package com.curtcox.snap.model;

import java.io.IOException;

import static com.curtcox.snap.model.Packet.*;

public final class SinkReader implements Sink, Reader {

    private final ConcurrentPacketList packets = new ConcurrentPacketList();

    @Override
    public Packet read(Filter filter) throws IOException {
        return packets.read(filter);
    }

    @Override
    public boolean add(Packet packet) {
        return packets.add(packet);
    }
}
