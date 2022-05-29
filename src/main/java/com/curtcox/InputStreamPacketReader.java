package com.curtcox;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static com.curtcox.Check.notNull;

final class InputStreamPacketReader implements Packet.Reader {

    final InputStream input;
    // https://en.wikipedia.org/wiki/Maximum_transmission_unit
    final byte[] buffer = new byte[2304];

    InputStreamPacketReader(InputStream input) {
        this.input = notNull(input);
    }

    public Packet read() throws IOException {
        int count = input.read(buffer);
        if (count<0) {
            return null;
        }
        byte[] raw = Arrays.copyOf(buffer,count);
        Bytes bytes = new Bytes(raw);
        if (!bytes.startsWith(Packet.MAGIC)) {
            throw new IOException("Snap packet expected");
        }
        int skip1 = Packet.MAGIC.length;
        String topic = stringAt(raw,skip1 + 2,raw[skip1+1]);
        int skip2 = skip1 + 2 + topic.length();
        String message = stringAt(raw,skip2 + 2,raw[skip2+1]);
        return new Packet(topic, message);
    }

    private String stringAt(byte[] raw, int offset, int length) {
        return new String(raw, offset, length, StandardCharsets.UTF_8);
    }
}
