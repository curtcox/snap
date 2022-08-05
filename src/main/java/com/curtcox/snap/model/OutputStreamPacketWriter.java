package com.curtcox.snap.model;

import java.io.*;
import java.nio.charset.StandardCharsets;

import static com.curtcox.snap.model.Bytes.*;
import static com.curtcox.snap.util.Check.notNull;

final class OutputStreamPacketWriter implements Packet.Writer {

    private final OutputStream output;

    OutputStreamPacketWriter(OutputStream output) {
        this.output = notNull(output);
    }

    public void write(Packet packet) throws IOException {
        output.write(Bytes.from(
                Packet.MAGIC.value(),
                longToBytes(packet.timestamp),
                longToBytes(packet.trigger.toLong()),
                sizePlusValue(packet.sender),
                sizePlusValue(packet.topic),
                sizePlusValue(packet.message)
        ).value());
    }

    static byte[] sizePlusValue(String value) {
        int length = value.length();
        byte hi = (byte) (length / 256);
        byte lo = (byte) (length % 0xFF);
        return Bytes.from(
                bytes(hi,lo).value(),
                value.getBytes(StandardCharsets.UTF_8)
        ).value();
    }

    static byte[] longToBytes(long l) {
        byte[] result = new byte[Long.BYTES];
        for (int i = Long.BYTES - 1; i >= 0; i--) {
            result[i] = (byte)(l & 0xFF);
            l >>= Byte.SIZE;
        }
        return result;
    }
}
