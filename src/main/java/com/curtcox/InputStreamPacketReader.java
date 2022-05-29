package com.curtcox;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import static com.curtcox.Check.notNull;

final class InputStreamPacketReader implements Packet.Reader {

    final InputStream input;
    final byte[] buffer = new byte[100 * 1000];

    InputStreamPacketReader(InputStream input) {
        this.input = notNull(input);
    }

    public Packet read() throws IOException {
        int count = input.read(buffer);
        if (count<0) {
            return null;
        }
        Bytes raw = new Bytes(Arrays.copyOf(buffer,count));
        if (!raw.startsWith(Packet.MAGIC)) {
            throw new IOException("Snap packet expected");
        }
        return new Packet("","");
    }
}
