package com.curtcox;

import java.io.IOException;
import java.io.OutputStream;

import static com.curtcox.Check.notNull;

final class OutputStreamPacketWriter implements Packet.Writer {

    private final OutputStream output;

    OutputStreamPacketWriter(OutputStream output) {
        this.output = notNull(output);
    }

    public void write(Packet packet) throws IOException {
        output.write(Packet.MAGIC.value());
    }
}
