package com.curtcox;

import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static com.curtcox.Random.random;
import static com.curtcox.TestUtil.tick;
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

    Packet packet = new Packet(topic,message);

    @Rule
    public Timeout globalTimeout = Timeout.seconds(3);

    @After
    public void stop() {
        runner.stop();
    }

    @Test
    public void messages_can_be_sent_from_a_node_thru_a_network() {
        write_a_packet_to_node_3();

        assertPacket(node1.read().next());
        assertPacket(node2.read().next());
        assertPacket(node4.read().next());
    }

    @Test
    public void messages_can_be_sent_from_a_reader_writer_thru_a_network() {
        write_a_packet_to_reader_writer();

        assertPacket(node1.read().next());
        assertPacket(node2.read().next());
        assertPacket(node3.read().next());
        assertPacket(node4.read().next());
    }

    @Test
    public void messages_can_be_read_from_a_reader_writer_thru_a_network() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PacketReaderWriter readerWriter = PacketReaderWriter.from(new ByteArrayInputStream(new byte[0]),out);
        network.add(readerWriter);
        write_a_packet_to_reader_writer();

        assertPacket(node1.read().next());
        assertPacket(node2.read().next());
        assertPacket(node3.read().next());
        assertPacket(node4.read().next());
        InputStreamPacketReader reader = new InputStreamPacketReader(new ByteArrayInputStream(out.toByteArray()));
        assertPacket(reader.read());
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
