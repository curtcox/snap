package com.curtcox.snap.model;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.*;

import static com.curtcox.snap.model.TestClock.tick;
import static com.curtcox.snap.model.Packet.ANY;
import static com.curtcox.snap.model.Random.random;
import static org.junit.Assert.*;
import static com.curtcox.snap.model.Bytes.*;

public class OutputStreamPacketWriterTest {

    Packet.Topic topic = Random.topic();
    String message = random("message");
    Packet.Sender sender = Random.sender();

    Packet packet = Packet.builder().sender(sender).topic(topic).message(message).build();

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
    public void packets_follow_magic_with_timestamp_sender_two_byte_topic_length_and_topic() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputStreamPacketWriter writer = new OutputStreamPacketWriter(outputStream);

        writer.write(packet);

        Bytes bytes = new Bytes(outputStream.toByteArray());
        assertTrue(bytes.startsWith(Bytes.from(
                Packet.MAGIC.value(),
                from(packet.timestamp.value).value(),
                from(packet.trigger.toLong()).value(),
                bytes(0,sender.value.length()).value(),
                sender.value.getBytes(StandardCharsets.UTF_8),
                bytes(0,topic.value.length()).value(),
                topic.value.getBytes(StandardCharsets.UTF_8)
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
                from(packet.timestamp.value).value(),
                from(packet.trigger.toLong()).value(),
                bytes(0,sender.value.length()).value(),
                sender.value.getBytes(StandardCharsets.UTF_8),
                bytes(0,topic.value.length()).value(),
                topic.value.getBytes(StandardCharsets.UTF_8),
                bytes(0,message.length()).value(),
                message.getBytes(StandardCharsets.UTF_8)
        ),bytes);
    }

    @Test
    public void packets_with_long_topic_and_message() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputStreamPacketWriter writer = new OutputStreamPacketWriter(outputStream);

        Packet packet = packetWithLongTopicAndMessage();
        String topic = packet.topic.value;
        String message = packet.message;
        String sender = packet.sender.value;
        writer.write(packet);

        Bytes bytes = new Bytes(outputStream.toByteArray());
        int expectedLength = Packet.MAGIC.length + Long.BYTES + Long.BYTES + sender.length() + topic.length() + message.length() + (3 * 2);
        assertEquals(expectedLength, bytes.length);
        assertEquals(Bytes.from(
                Packet.MAGIC.value(),
                from(packet.timestamp.value).value(),
                from(packet.trigger.toLong()).value(),
                bytes(1,sender.length() % 0xFF).value(),
                sender.getBytes(StandardCharsets.UTF_8),
                bytes(1,topic.length() % 0xFF).value(),
                topic.getBytes(StandardCharsets.UTF_8),
                bytes(1,message.length() % 0xFF).value(),
                message.getBytes(StandardCharsets.UTF_8)
        ),bytes);
    }

    @Test
    public void can_write_packet() throws Exception {
        PipedOutputStream externalInput = new PipedOutputStream();
        PipedInputStream externalOutput = new PipedInputStream(externalInput,Packet.MAX_SIZE);
        OutputStreamPacketWriter writer = new OutputStreamPacketWriter(externalInput);
        InputStreamPacketReader reader = new InputStreamPacketReader(externalOutput);

        ExecutorService service = Executors.newSingleThreadExecutor();
        Future<Packet> submitted = service.submit(() -> reader.read(ANY));
        writer.write(packet);
        tick(2);
        Packet read = submitted.get();

        assertEquals(packet.sender,read.sender);
        assertEquals(topic,read.topic);
        assertEquals(message,read.message);
    }

    @Test
    public void can_write_packet_with_long_topic_and_message() throws Exception {
        PipedOutputStream externalInput = new PipedOutputStream();
        PipedInputStream externalOutput = new PipedInputStream(externalInput,Packet.MAX_SIZE);
        OutputStreamPacketWriter writer = new OutputStreamPacketWriter(externalInput);
        InputStreamPacketReader reader = new InputStreamPacketReader(externalOutput);

        Packet packet = packetWithLongTopicAndMessage();

        ExecutorService service = Executors.newSingleThreadExecutor();
        Future<Packet> submitted = service.submit(() -> Await.packet(reader));
        writer.write(packet);
        Packet read = submitted.get();

        assertEquals(packet.sender,read.sender);
        assertEquals(packet.topic,read.topic);
        assertEquals(packet.message,read.message);
    }

    private Packet packetWithLongTopicAndMessage() {
        String very = "Very, very, very, very, very, very, very, very, very, very, very, very, very, very, very, very,";
        String pad = toString() + new Date() + System.currentTimeMillis();
        String topic = very + very + " long topic or at least greater than 255 characters " + pad;
        String message = very + very + " long message or at least greater than 255 characters" + pad;
        String sender = very + very + " long sender or at least greater than 255 characters" + pad;
        assertTrue(sender.length()>255);
        assertTrue(topic.length()>255);
        assertTrue(message.length()>255);
        Packet packet = Packet.builder()
                .sender(new Packet.Sender(sender))
                .topic(new Packet.Topic(topic))
                .message(message)
                .build();
        return packet;
    }

    @Test
    public void can_read_1_packet_written_to_bytes() throws IOException {
        Packet packet = Random.packet();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        OutputStreamPacketWriter.writeTo(out,packet);
        List<Packet> packets = InputStreamPacketReader.readWaiting(new ByteArrayInputStream(out.toByteArray()));

        assertEquals(1,packets.size());
        assertEquals(packet,packets.get(0));
    }

    @Test
    public void can_read_2_packets_written_to_bytes() throws IOException {
        Packet packet1 = Random.packet();
        Packet packet2 = Random.packet();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        OutputStreamPacketWriter.writeTo(out,packet1,packet2);
        List<Packet> packets = InputStreamPacketReader.readWaiting(new ByteArrayInputStream(out.toByteArray()));

        assertEquals(2,packets.size());
        assertEquals(packet1,packets.get(0));
        assertEquals(packet2,packets.get(1));
    }

    @Test
    public void can_read_9_packets_written_to_bytes() throws IOException {
        can_read_x_packets_written_to_bytes(9);
    }

    public void can_read_x_packets_written_to_bytes(int size) throws IOException {
        List<Packet> packets = new ArrayList<>();
        for (int i=0; i<size; i++) {
            packets.add(Random.packet());
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        OutputStreamPacketWriter.writeTo(out,packets.toArray(new Packet[0]));
        List<Packet> read = InputStreamPacketReader.readWaiting(new ByteArrayInputStream(out.toByteArray()));

        assertEquals(size,read.size());
        assertEquals(packets,read);
    }

}
