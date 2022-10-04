package com.curtcox.snap.model;

import java.io.OutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static com.curtcox.snap.model.Bytes.*;
import static com.curtcox.snap.util.Check.notNull;
import static com.curtcox.snap.model.Packet.*;

/**
 * A Packet.Writer that writes to an OutputStream.
 */
public final class OutputStreamPacketWriter implements Writer {

    private final OutputStream output;

    public OutputStreamPacketWriter(OutputStream output) {
        this.output = notNull(output);
    }

    public void write(Packet packet) throws IOException {
        output.write(Bytes.from(
                Packet.MAGIC.value(),
                longToBytes(packet.timestamp.value),
                longToBytes(packet.trigger.toLong()),
                sizePlusValue(packet.sender.toString()),
                sizePlusValue(packet.topic.toString()),
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

    public static void writeTo(OutputStream out, Packet... packets) throws IOException {
        OutputStreamPacketWriter writer = new OutputStreamPacketWriter(out);
        for (Packet packet : packets) {
            writer.write(packet);
        }
    }

}
