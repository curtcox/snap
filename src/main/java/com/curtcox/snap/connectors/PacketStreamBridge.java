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

    static final boolean debug = Debug.on;

    static PacketStreamBridge fromServerSocket(ServerSocket socket, Runner runner) {
        PacketStreamBridge streams = new PacketStreamBridge();
        SimpleServerSocket serverSocket = SimpleServerSocket.forTCP(socket,streams,runner);
//        serverSocket.start(runner); // TODO FIXME <-- Directly test this hard-to-find bug
        return streams;
    }

    @Override
    public void accept(StreamIO streamIO) {
        if (debug) debug("accepted " + streamIO);
        streams.add(streamIO);
    }

    @Override
    public Packet read(Packet.Filter filter) throws IOException {
        for (StreamIO streamIO : streams) {
            Packet packet = read(streamIO,filter);
            if (packet!=null) {
                return packet;
            }
        }
        return null;
    }

    @Override
    public void write(Packet packet) throws IOException {
        for (StreamIO streamIO : streams) {
            write(streamIO,packet);
        }
    }

    private static void write(StreamIO streamIO, Packet packet) throws IOException {
        if (debug) debug("writing " + packet + " to " + streamIO);
        new OutputStreamPacketWriter(streamIO.out).write(packet);
    }

    private static Packet read(StreamIO streamIO,Packet.Filter filter) throws IOException {
        Packet packet = new InputStreamPacketReader(streamIO.in).read(filter);
        if (debug) debug("read " + packet + " from " + streamIO);
        return packet;
    }

    private static void debug(String message) {
        System.out.println("Bridge " + message);
        new Throwable(message).printStackTrace();
    }
}
