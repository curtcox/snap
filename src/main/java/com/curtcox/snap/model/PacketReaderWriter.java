package com.curtcox.snap.model;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static com.curtcox.snap.util.Check.notNull;
import static com.curtcox.snap.model.Packet.*;

public final class PacketReaderWriter implements IO {

    final Reader input;
    final Writer output;

    PacketReaderWriter(Reader input, Writer output) {
        this.input = notNull(input);
        this.output = notNull(output);
    }

    public static PacketReaderWriter from(InputStream in, OutputStream out) {
        return new PacketReaderWriter(new InputStreamPacketReader(in),new OutputStreamPacketWriter(out));
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
