package com.curtcox;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import static com.curtcox.Bytes.bytes;
import static com.curtcox.OutputStreamPacketWriter.sizePlusValue;
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

    @Test
    public void read_short_topic_and_message() throws IOException {
        Packet packet = read(Bytes.from(
                Packet.MAGIC.value(),
                bytes(sizePlusValue(topic)).value(),
                bytes(sizePlusValue(message)).value()
        ));
        assertEquals(topic,packet.topic);
        assertEquals(message,packet.message);
    }

    @Test
    public void read_empty_topic_and_message() throws IOException {
        String topic = "";
        String message = "";
        Packet packet = read(Bytes.from(
                Packet.MAGIC.value(),
                bytes(sizePlusValue(topic)).value(),
                bytes(sizePlusValue(message)).value()
        ));
        assertEquals(topic,packet.topic);
        assertEquals(message,packet.message);
    }

    @Test
    public void read_long_topic_and_message_1() throws IOException {
        assertBigPacket(1);
    }

    @Test
    public void read_long_topic_and_message_2() throws IOException {
        assertBigPacket(2);
    }

    @Test
    public void read_long_topic_and_message_x() throws IOException {
        for (int i=0; i<10; i++) {
            assertBigPacket(i);
        }
    }

    private void assertBigPacket(int count) throws IOException {
        String dots = "...............................................................................................";
        String pad = "";
        for (int i=0; i<count; i++) {
            pad = pad + dots;
        }
        String topic = "X" + pad + "X";
        String message = "Y" + pad + "Y";
        Bytes bytes = Bytes.from(
                Packet.MAGIC.value(),
                bytes(sizePlusValue(topic)).value(),
                bytes(sizePlusValue(message)).value()
        );
        int expectedLength = Packet.MAGIC.length + topic.length() + message.length() + 4;
        assertEquals(expectedLength,bytes.length);
        Packet packet = read(bytes);
        assertEquals(topic,packet.topic);
        assertEquals(message,packet.message);
    }

}
