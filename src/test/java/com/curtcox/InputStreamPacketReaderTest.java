package com.curtcox;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import static com.curtcox.Bytes.bytes;
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
        PipedInputStream externalOutput = new PipedInputStream(externalInput);
        OutputStreamPacketWriter writer = new OutputStreamPacketWriter(externalInput);
        InputStreamPacketReader reader = new InputStreamPacketReader(externalOutput);

        writer.write(packet);
        Packet read = reader.read();

        assertEquals(topic,read.topic);
        assertEquals(message,read.message);
    }

    private Packet read(Bytes bytes) throws IOException {
        return new InputStreamPacketReader(new ByteArrayInputStream(bytes.value())).read();
    }

    @Test(expected = IOException.class)
    public void read_throws_exception_when_packet_does_not_start_with_magic() throws IOException {
        read(bytes(1,2,3,4));
    }

    @Test
    public void read_returns_null_when_stream_is_empty() throws IOException {
        assertNull(read(bytes()));
    }

}
