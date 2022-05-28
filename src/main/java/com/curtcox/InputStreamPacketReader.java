package com.curtcox;

import java.io.InputStream;

import static com.curtcox.Check.notNull;

final class InputStreamPacketReader implements Packet.Reader {

    final InputStream input;

    InputStreamPacketReader(InputStream input) {
        this.input = notNull(input);
    }

    public Packet read() {
        return null;
    }
}
