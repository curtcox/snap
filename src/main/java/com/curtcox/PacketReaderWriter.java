package com.curtcox;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static com.curtcox.Check.notNull;

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
    public Packet read() throws IOException {
        return input.read();
    }

    @Override
    public void write(Packet packet) throws IOException {
        output.write(packet);
    }
}
