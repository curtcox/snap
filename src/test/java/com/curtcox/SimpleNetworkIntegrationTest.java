package com.curtcox;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import static com.curtcox.Random.random;
import static com.curtcox.TestUtil.shortDelay;
import static org.junit.Assert.*;

public class SimpleNetworkIntegrationTest {

    SimpleNetwork network = SimpleNetwork.newPolling();
    Node node1 = Node.on(network);
    Node node2 = Node.on(network);
    Node node3 = Node.on(network);
    Node node4 = Node.on(network);

    String topic = random("topic");
    String message = random("message");

    Packet packet = new Packet(topic,message);


    @Rule
    public Timeout globalTimeout = Timeout.seconds(3);

    @Test
    public void messages_can_be_sent_from_a_node_thru_a_network() {
        write_a_packet_to_node_3();

        assertPacket(node1.read());
        assertPacket(node2.read());
        assertPacket(node4.read());
    }

    @Test
    public void messages_can_be_sent_from_a_reader_writer_thru_a_network() {
        write_a_packet_to_reader_writer();

        assertPacket(node1.read());
        assertPacket(node2.read());
        assertPacket(node3.read());
        assertPacket(node4.read());
    }

    private void write_a_packet_to_reader_writer() {
        Bytes bytes = packet.asBytes();
        PacketReaderWriter readerWriter = PacketReaderWriter.from(new ByteArrayInputStream(bytes.value()),new ByteArrayOutputStream());
        network.add(readerWriter);
        shortDelay();
    }

    private void write_a_packet_to_node_3() {
        node3.write(packet);
        shortDelay();
    }

    private void assertPacket(Packet packet) {
        assertNotNull(packet);
        assertEquals(topic,packet.topic);
        assertEquals(message,packet.message);
    }
}
