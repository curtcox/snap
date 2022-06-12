package com.curtcox;

import java.io.IOException;

import static com.curtcox.Check.notNull;

final class PacketReaderWriter implements Packet.IO {

    final Packet.Reader input;
    final Packet.Writer output;

    PacketReaderWriter(Packet.Reader input, Packet.Writer output) {
        this.input = notNull(input);
        this.output = notNull(output);
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
