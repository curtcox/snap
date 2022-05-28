package com.curtcox;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import static com.curtcox.Random.random;
import static org.junit.Assert.*;

public class OutputStreamPacketWriterTest {

    String topic = random("topic");
    String message = random("message");

    Packet packet = new Packet(topic,message);

    @Rule
    public Timeout globalTimeout = Timeout.seconds(2);

    @Test
    public void packets_start_with_magic() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputStreamPacketWriter writer = new OutputStreamPacketWriter(outputStream);

        writer.write(packet);

        Bytes bytes = new Bytes(outputStream.toByteArray());
        assertTrue(bytes.startsWith(Packet.MAGIC));
    }

    @Test
    public void can_write_packet() throws IOException {
        PipedOutputStream externalInput = new PipedOutputStream();
        PipedInputStream externalOutput = new PipedInputStream();
        OutputStreamPacketWriter writer = new OutputStreamPacketWriter(externalInput);
        InputStreamPacketReader reader = new InputStreamPacketReader(externalOutput);

        writer.write(packet);
        Packet read = reader.read();

        assertEquals(topic,read.topic);
        assertEquals(message,read.message);
    }
}
