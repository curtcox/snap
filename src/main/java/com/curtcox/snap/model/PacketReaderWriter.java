package com.curtcox.snap.model;

import java.io.*;

import static com.curtcox.snap.util.Check.notNull;

final class PacketReaderWriter implements Packet.IO {

    final Packet.Reader input;
    final Packet.Writer output;

    PacketReaderWriter(Packet.Reader input, Packet.Writer output) {
        this.input = notNull(input);
        this.output = notNull(output);
    }

    static PacketReaderWriter from(InputStream in, OutputStream out) {
        return new PacketReaderWriter(new InputStreamPacketReader(in),new OutputStreamPacketWriter(out));
    }

    @Override
    public Packet read(Packet.Filter filter) throws IOException {
        return input.read(filter);
    }

    @Override
    public void write(Packet packet) throws IOException {
        output.write(packet);
    }
}
