package com.curtcox;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import static com.curtcox.Bytes.bytes;
import static com.curtcox.Check.notNull;

final class OutputStreamPacketWriter implements Packet.Writer {

    private final OutputStream output;

    OutputStreamPacketWriter(OutputStream output) {
        this.output = notNull(output);
    }

    public void write(Packet packet) throws IOException {
        String topic = packet.topic;
        String message = packet.message;
        output.write(Bytes.from(
                Packet.MAGIC.value(),
                sizePlusValue(topic),
                sizePlusValue(message)
        ).value());
    }

    private byte[] sizePlusValue(String value) {
        return Bytes.from(
                bytes(0,value.length()).value(),
                value.getBytes(StandardCharsets.UTF_8)
        ).value();
    }
}
