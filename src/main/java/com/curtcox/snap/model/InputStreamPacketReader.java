package com.curtcox.snap.model;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static com.curtcox.snap.util.Check.notNull;

final class InputStreamPacketReader implements Packet.Reader {

    final InputStream input;
    final byte[] buffer = new byte[Packet.MAX_SIZE];

    InputStreamPacketReader(InputStream input) {
        this.input = notNull(input);
    }

    public Packet read(Packet.Filter filter) throws IOException {
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
        long timestamp = longAt(raw,skip1);
        int skip2 = skip1 + Long.BYTES;
        long trigger = longAt(raw,skip2);
        int skip3 = skip2 + Long.BYTES;
        String sender = stringAt(raw,skip3);
        int skip4 = skip3 + 2 + sender.length();
        String topic = stringAt(raw,skip4);
        int skip5 = skip4 + 2 + topic.length();
        String message = stringAt(raw,skip5);
        return new Packet(sender,topic,message,timestamp,trigger);
    }

    public static long longAt(final byte[] b, int offset) {
        long result = 0;
        for (int i = 0; i < Long.BYTES; i++) {
            result <<= Byte.SIZE;
            result |= (b[i + offset] & 0xFF);
        }
        return result;
    }

    private String stringAt(byte[] raw, int offset) {
        int hi = raw[offset] & 0xFF;
        int lo = raw[offset + 1] & 0xFF;
        int length = hi * 255 + lo;
        return new String(raw, offset + 2, length, StandardCharsets.UTF_8);
    }
}
