package com.curtcox;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import static com.curtcox.Random.random;
import static org.junit.Assert.*;

public class InputStreamPacketReaderTest {

    String topic = random("topic");
    String message = random("message");

    @Rule
    public Timeout globalTimeout = Timeout.seconds(2);

    @Test
    public void can_read_packet() throws IOException {
        Packet packet = new Packet(topic,message);
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
