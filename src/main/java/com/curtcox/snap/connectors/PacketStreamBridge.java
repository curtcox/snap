package com.curtcox.snap.connectors;

import com.curtcox.snap.model.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * A Packet.IO that accepts StreamIOs.
 * For bridging between a Packet.IO and an evolving set of StreamIOs.
 */
final class PacketStreamBridge implements Consumer<StreamIO>, Packet.IO {

    final List<StreamIO> streams = new ArrayList<>();

    static PacketStreamBridge fromServerSocket(ServerSocket socket, Runner runner) {
        PacketStreamBridge streams = new PacketStreamBridge();
        SimpleServerSocket serverSocket = SimpleServerSocket.forTCP(socket,streams);
        serverSocket.start(runner);
        return streams;
    }

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
