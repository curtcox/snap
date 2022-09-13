package com.curtcox.snap.model;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import static com.curtcox.snap.util.Check.notNull;
import static com.curtcox.snap.model.Packet.*;

final class InputStreamPacketReader implements Reader {

    final InputStream input;
    final byte[] buffer = new byte[Packet.MAX_SIZE];

    InputStreamPacketReader(InputStream input) {
        this.input = notNull(input);
    }

    public Packet read(Filter filter) throws IOException {
        if (input.available()<1) {
            return null;
        }
        int count = input.read(buffer);
        if (count<0) {
            return null;
        }
        byte[] raw = Arrays.copyOf(buffer,count);
        Bytes bytes = new Bytes(raw);
        return Packet.from(bytes);
    }

}
