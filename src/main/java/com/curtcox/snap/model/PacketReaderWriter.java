package com.curtcox.snap.model;

import com.curtcox.snap.connectors.StreamIO;

import java.io.IOException;

import static com.curtcox.snap.util.Check.notNull;
import static com.curtcox.snap.model.Packet.*;

public final class PacketReaderWriter implements IO {

    final Reader input;
    final Writer output;

    public PacketReaderWriter(Reader input, Writer output) {
        this.input = notNull(input);
        this.output = notNull(output);
    }

    public static PacketReaderWriter from(StreamIO io) {
        return new PacketReaderWriter(new InputStreamPacketReader(io.in),new OutputStreamPacketWriter(io.out));
    }

    @Override
    public Packet read(Filter filter) throws IOException {
        return input.read(filter);
    }

    @Override
    public void write(Packet packet) throws IOException {
        output.write(packet);
    }
}
