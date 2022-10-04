package com.curtcox.snap.connectors;

import com.curtcox.snap.model.InputStreamPacketReader;
import com.curtcox.snap.model.OutputStreamPacketWriter;
import com.curtcox.snap.model.Packet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * A Packet.IO that accepts StreamIOs.
 * For bridging between a Packet.IO and an evolving set of StreamIOs.
 */
final class PacketStreamBridge implements Consumer<StreamIO>, Packet.IO {

    final List<StreamIO> streams = new ArrayList<>();

    @Override
    public void accept(StreamIO streamIO) {
        streams.add(streamIO);
    }

    @Override
    public Packet read(Packet.Filter filter) throws IOException {
        for (StreamIO streamIO : streams) {
            Packet packet = new InputStreamPacketReader(streamIO.in).read(filter);
            if (packet!=null) {
                return packet;
            }
        }
        return null;
    }

    @Override
    public void write(Packet packet) throws IOException {
        for (StreamIO streamIO : streams) {
            new OutputStreamPacketWriter(streamIO.out).write(packet);
        }
    }
}
