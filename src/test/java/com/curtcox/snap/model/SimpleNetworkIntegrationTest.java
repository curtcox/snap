package com.curtcox.snap.model;

import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static com.curtcox.snap.model.Clock.tick;
import static com.curtcox.snap.model.Random.random;
import static org.junit.Assert.*;

public class SimpleNetworkIntegrationTest {

    Runner runner = Runner.of();
    SimpleNetwork network = SimpleNetwork.newPolling(runner);
    Node node1 = Node.on(network);
    Node node2 = Node.on(network);
    Node node3 = Node.on(network);
    Node node4 = Node.on(network);

    String topic = random("topic");
    String message = random("message");

    String sender = random("sender");

    Packet packet = new Packet(sender,topic,message);

    @Rule
    public Timeout globalTimeout = Timeout.seconds(3);

    @After
    public void stop() {
        runner.stop();
    }

    @Test
    public void messages_can_be_sent_from_a_node_thru_a_network() throws IOException {
        write_a_packet_to_node_3();

        assertPacket(read(node1));
        assertPacket(read(node2));
        assertPacket(read(node4));
    }

    private Packet read(Node node) throws IOException {
        return node.read(Packet.ANY).read(Packet.ANY);
    }
    @Test
    public void messages_can_be_sent_from_a_reader_writer_thru_a_network() throws IOException {
        write_a_packet_to_reader_writer();

        assertPacket(read(node1));
        assertPacket(read(node2));
        assertPacket(read(node3));
        assertPacket(read(node4));
    }

    @Test
    public void messages_can_be_read_from_a_reader_writer_thru_a_network() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PacketReaderWriter readerWriter = PacketReaderWriter.from(new ByteArrayInputStream(new byte[0]),out);
        network.add(readerWriter);
        write_a_packet_to_reader_writer();

        assertPacket(read(node1));
        assertPacket(read(node2));
        assertPacket(read(node3));
        assertPacket(read(node4));
        InputStreamPacketReader reader = new InputStreamPacketReader(new ByteArrayInputStream(out.toByteArray()));
        assertPacket(reader.read(Packet.ANY));
    }

    private void write_a_packet_to_reader_writer() {
        Bytes bytes = packet.asBytes();
        PacketReaderWriter readerWriter = PacketReaderWriter.from(new ByteArrayInputStream(bytes.value()),new ByteArrayOutputStream());
        network.add(readerWriter);
        tick(3);
    }

    private void write_a_packet_to_node_3() {
        node3.write(packet);
        tick(2);
    }

    private void assertPacket(Packet packet) {
        assertNotNull(packet);
        assertEquals(topic,packet.topic);
        assertEquals(message,packet.message);
    }
}
