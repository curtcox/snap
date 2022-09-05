package com.curtcox.snap.model;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import static com.curtcox.snap.model.Bytes.*;
import static com.curtcox.snap.model.OutputStreamPacketWriter.sizePlusValue;
import static com.curtcox.snap.model.Random.random;
import static com.curtcox.snap.model.Packet.*;
import static org.junit.Assert.*;

public class InputStreamPacketReaderTest {

    Packet.Topic topic = Random.topic();
    String message = random("message");
    Packet.Sender sender = Random.sender();
    Timestamp timestamp = Timestamp.now();
    Trigger trigger = randomTrigger();

    private static Packet.Trigger randomTrigger() {
        return Packet.Trigger.from(System.currentTimeMillis());
    }

    @Rule
    public Timeout globalTimeout = Timeout.seconds(2);

    @Test
    public void can_read_packet() throws IOException {
        Packet packet = Packet.builder()
                .sender(sender).topic(topic).message(message).timestamp(timestamp).trigger(trigger)
                .build();
        PipedOutputStream externalInput = new PipedOutputStream();
        PipedInputStream externalOutput = new PipedInputStream(externalInput);
        OutputStreamPacketWriter writer = new OutputStreamPacketWriter(externalInput);
        InputStreamPacketReader reader = new InputStreamPacketReader(externalOutput);

        writer.write(packet);
        Packet read = reader.read(Packet.ANY);

        assertEquals(timestamp,read.timestamp);
        assertEquals(trigger,read.trigger);
        assertEquals(sender,read.sender);
        assertEquals(topic,read.topic);
        assertEquals(message,read.message);
    }

    private Packet read(Bytes bytes) throws IOException {
        return new InputStreamPacketReader(new ByteArrayInputStream(bytes.value())).read(Packet.ANY);
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
                from(timestamp.value).value(),
                from(trigger.toLong()).value(),
                bytes(sizePlusValue(sender.value)).value(),
                bytes(sizePlusValue(topic.value)).value(),
                bytes(sizePlusValue(message)).value()
        ));
        assertEquals(timestamp,packet.timestamp);
        assertEquals(trigger,packet.trigger);
        assertEquals(sender,packet.sender);
        assertEquals(topic,packet.topic);
        assertEquals(message,packet.message);
    }

    @Test
    public void read_empty_topic_and_message() throws IOException {
        Packet packet = read(Bytes.from(
                Packet.MAGIC.value(),
                bytes(new byte[Long.BYTES]).value(),
                bytes(new byte[Long.BYTES]).value(),
                bytes(sizePlusValue("")).value(),
                bytes(sizePlusValue("")).value(),
                bytes(sizePlusValue("")).value()
        ));
        assertEquals(0L,packet.timestamp.value);
        assertEquals(0L,packet.trigger.toLong());
        assertEquals("",packet.sender.value);
        assertEquals("",packet.topic.value);
        assertEquals("",packet.message);
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
    public void read_long_topic_and_message_3() throws IOException {
        assertBigPacket(3);
    }

    @Test
    public void read_long_topic_and_message_7() throws IOException {
        assertBigPacket(7);
    }

    @Test
    public void read_long_topic_and_message_x() throws IOException {
        for (int i=0; i<8; i++) {
            assertBigPacket(i);
        }
    }

    private void assertBigPacket(int count) throws IOException {
        String dots = "...............................................................................................";
        String pad = "";
        for (int i=0; i<count; i++) {
            pad = pad + dots;
        }
        String sender = "T" + pad + "O";
        String topic = "X" + pad + "X";
        String message = "Y" + pad + "Y";
        long timestamp = System.currentTimeMillis();
        long trigger = System.currentTimeMillis();
        Bytes bytes = Bytes.from(
                Packet.MAGIC.value(),
                from(timestamp).value(),
                from(trigger).value(),
                bytes(sizePlusValue(sender)).value(),
                bytes(sizePlusValue(topic)).value(),
                bytes(sizePlusValue(message)).value()
        );
        int expectedLength = Packet.MAGIC.length + Long.BYTES + Long.BYTES + sender.length() + topic.length() + message.length() + (3 * 2);
        assertEquals(expectedLength,bytes.length);
        Packet packet = read(bytes);
        assertEquals(timestamp,packet.timestamp.value);
        assertEquals(trigger,packet.trigger.toLong());
        assertEquals(sender,packet.sender.value);
        assertEquals(topic,packet.topic.value);
        assertEquals(message,packet.message);
    }

}
