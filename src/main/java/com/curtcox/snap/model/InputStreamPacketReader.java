package com.curtcox.snap.model;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static com.curtcox.snap.util.Check.notNull;

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
        String sender = stringAt(raw,skip1);
        int skip2 = skip1 + 2 + sender.length();
        String topic = stringAt(raw,skip2);
        int skip3 = skip2 + 2 + topic.length();
        String message = stringAt(raw,skip3);
        return new Packet(sender,topic,message);
    }

    private String stringAt(byte[] raw, int offset) {
        int hi = raw[offset] & 0xFF;
        int lo = raw[offset + 1] & 0xFF;
        int length = hi * 255 + lo;
        return new String(raw, offset + 2, length, StandardCharsets.UTF_8);
    }
}
