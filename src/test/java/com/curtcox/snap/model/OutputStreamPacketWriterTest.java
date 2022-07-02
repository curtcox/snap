package com.curtcox.snap.model;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import static com.curtcox.snap.model.Random.random;
import static org.junit.Assert.*;
import static com.curtcox.snap.model.Bytes.bytes;

public class OutputStreamPacketWriterTest {

    String topic = random("topic");
    String message = random("message");
    String sender = random("sender");

    Packet packet = new Packet(sender,topic,message);

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
    public void packets_follow_magic_with_two_byte_topic_length_and_topic() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputStreamPacketWriter writer = new OutputStreamPacketWriter(outputStream);

        writer.write(packet);

        Bytes bytes = new Bytes(outputStream.toByteArray());
        assertTrue(bytes.startsWith(Bytes.from(
                Packet.MAGIC.value(),
                bytes(0,topic.length()).value(),
                topic.getBytes(StandardCharsets.UTF_8)
        )));
    }

    @Test
    public void packets_follow_topic_with_two_byte_length_and_message() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputStreamPacketWriter writer = new OutputStreamPacketWriter(outputStream);

        writer.write(packet);

        Bytes bytes = new Bytes(outputStream.toByteArray());
        assertEquals(Bytes.from(
                Packet.MAGIC.value(),
                bytes(0,topic.length()).value(),
                topic.getBytes(StandardCharsets.UTF_8),
                bytes(0,message.length()).value(),
                message.getBytes(StandardCharsets.UTF_8)
        ),bytes);
    }

    @Test
    public void packets_with_long_topic_and_message() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputStreamPacketWriter writer = new OutputStreamPacketWriter(outputStream);

        Packet packet = packetWithLongTopicAndMessage();
        String topic = packet.topic;
        String message = packet.message;
        writer.write(packet);

        Bytes bytes = new Bytes(outputStream.toByteArray());
        int expectedLength = Packet.MAGIC.length + topic.length() + message.length() + 4;
        assertEquals(expectedLength, bytes.length);
        assertEquals(Bytes.from(
                Packet.MAGIC.value(),
                bytes(1,topic.length() % 0xFF).value(),
                topic.getBytes(StandardCharsets.UTF_8),
                bytes(1,message.length() % 0xFF).value(),
                message.getBytes(StandardCharsets.UTF_8)
        ),bytes);
    }

    @Test
    public void can_write_packet() throws IOException {
        PipedOutputStream externalInput = new PipedOutputStream();
        PipedInputStream externalOutput = new PipedInputStream(externalInput);
        OutputStreamPacketWriter writer = new OutputStreamPacketWriter(externalInput);
        InputStreamPacketReader reader = new InputStreamPacketReader(externalOutput);

        writer.write(packet);
        Packet read = reader.read();

        assertEquals(topic,read.topic);
        assertEquals(message,read.message);
    }

    @Test
    public void can_write_packet_with_long_topic_and_message() throws IOException {
        PipedOutputStream externalInput = new PipedOutputStream();
        PipedInputStream externalOutput = new PipedInputStream(externalInput);
        OutputStreamPacketWriter writer = new OutputStreamPacketWriter(externalInput);
        InputStreamPacketReader reader = new InputStreamPacketReader(externalOutput);

        Packet packet = packetWithLongTopicAndMessage();

        writer.write(packet);
        Packet read = reader.read();

        assertEquals(packet.topic,read.topic);
        assertEquals(packet.message,read.message);
    }

    private Packet packetWithLongTopicAndMessage() {
        String very = "Very, very, very, very, very, very, very, very, very, very, very, very, very, very, very, very,";
        String pad = toString() + new Date() + System.currentTimeMillis();
        String topic = very + very + " long topic or at least greater than 255 characters " + pad;
        String message = very + very + " long message or at least greater than 255 characters" + pad;
        String sender = very + very + " long sender or at least greater than 255 characters" + pad;
        assertTrue(topic.length()>255);
        assertTrue(message.length()>255);
        Packet packet = new Packet(sender,topic,message);
        return packet;
    }
}
