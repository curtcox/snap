package com.curtcox.snap.model;

import java.io.IOException;

/**
 * So multiple readers can read from a single reader.
 * This is like a tee, but different in that a tee is a writer that writes to two different places.
 * This is a reader that allows multiple readers to read from it.
 * It is useful whenever multiple responses are required for a single packet that aren't
 * coordinated.
 * See also CombinedReader.
 */
final class SplitReaderFactory implements Packet.Reader.Factory {

    private final Packet.Reader source;
    private final PacketStream stream = new PacketStream();

    SplitReaderFactory(Packet.Reader reader) {
        this.source = reader;
    }

    @Override
    public Packet.Reader reader(Packet.Filter f1) {
        final Packet.Reader reader = new Packet.Reader() {

            PacketStream.Position last;
            @Override
            public Packet read(Packet.Filter f2) throws IOException {
                Packet.Filter filter = packet -> f1.passes(packet) && f2.passes(packet);
                Packet fromSource = source.read(filter);
                if (fromSource!=null) {
                    stream.add(fromSource);
                }
                PacketStream.PacketAndPosition packetAndPosition = stream.after(last,filter);
                if (packetAndPosition!=null) {
                    last = packetAndPosition.position;
                    return packetAndPosition.packet;
                }
                return null;
            }

        };
        return reader;
    }
}
