package com.curtcox.snap.connectors;

import com.curtcox.snap.model.Packet;

import java.io.IOException;
import java.util.function.Consumer;

final class PacketStreamBridge implements Consumer<StreamIO>, Packet.IO {

    @Override
    public void accept(StreamIO streamIO) {

    }

    @Override
    public Packet read(Packet.Filter filter) throws IOException {
        return null;
    }

    @Override
    public void write(Packet packet) throws IOException {

    }
}
