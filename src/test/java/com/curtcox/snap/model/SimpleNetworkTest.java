package com.curtcox.snap.model;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.io.IOException;

import static com.curtcox.snap.model.Clock.tick;
import static com.curtcox.snap.model.TestUtil.consume;
import static com.curtcox.snap.model.Packet.ANY;

import static org.junit.Assert.*;

public class SimpleNetworkTest {

    SimpleNetwork network = SimpleNetwork.newPolling();
    Node node1 = Node.on(network);
    Node node2 = Node.on(network);

    FakeIO io1 = new FakeIO();
    FakeIO io2 = new FakeIO();

    @Rule
    public Timeout globalTimeout = Timeout.seconds(2);

    @Test
    public void can_create() {
        assertNotNull(SimpleNetwork.newPolling());
    }

    @Test
    public void there_are_no_messages_to_listen_to_before_any_are_sent() throws IOException {
        assertNull(node1.read(ANY).read(ANY));
        assertNull(node2.read(ANY).read(ANY));
    }

    @Test
    public void messages_can_be_sent_over_a_network() throws IOException {
        Packet packet = Random.packet();
        node1.write(packet);
        tick(2);

        Packet read = node2.read(ANY).read(ANY);
        assertEquals(packet,read);
    }

    @Test
    public void messages_are_delivered_in_order_over_network() throws IOException {
        node1.write(packet("D","call","Mom"));
        node1.write(packet("C","call","Dad"));
        tick(3);
        Packet.Reader packets = node2.read(ANY);

        assertEquals("Mom", packets.read(ANY).message);
        assertEquals("Dad", packets.read(ANY).message);
    }

    @Test
    public void messages_can_be_read_only_once_by_receiver() throws IOException {
        node1.write(packet("me","phone","ring"));
        tick(2);

        assertNotNull(consume(node2));
        assertNull(node2.read(ANY).read(ANY));
    }

    @Test
    public void messages_can_be_read_at_multiple_points() throws IOException {
        Node node3 = Node.on(network);
        node1.write(packet("me","phone","ring"));
        tick(2);

        Packet packet2 = node2.read(ANY).read(ANY);
        assertEquals("me",packet2.sender.value);
        assertEquals("phone",packet2.topic.value);
        assertEquals("ring",packet2.message);

        Packet packet3 = node3.read(ANY).read(ANY);
        assertEquals("me",packet3.sender.value);
        assertEquals("phone",packet3.topic.value);
        assertEquals("ring",packet3.message);
    }


    @Test
    public void no_packets_are_written_to_IO_when_there_are_no_packets_to_read() {
        network.add(io1);
        network.add(io2);

        assertEquals(0,io1.written().size());
        assertEquals(0,io2.written().size());
    }

    @Test
    public void packet_is_written_to_IO_when_there_is_a_packet_to_read() {
        Packet packet = packet("me","room","on");
        io1.add(packet);

        network.add(io1);
        network.add(io2);

        tick(2);
        assertEquals(1,io2.written().size());
        assertEquals(packet,io2.written().get(0));
    }

    @Test
    public void packet_is_written_to_IO_even_if_no_packet_is_initially_available() {
        network.add(io1);
        network.add(io2);

        Packet packet = packet("me","room","on");
        io1.add(packet);

        tick(2);
        assertEquals(1,io2.written().size());
        assertEquals(packet,io2.written().get(0));
    }

    @Test
    public void messages_from_IO_are_delived_to_nodes() throws IOException {
        Packet packet = packet("me","room","on");
        io1.add(packet);

        network.add(io1);
        network.add(io2);

        tick(2);

        Packet packet1 = consume(node1);
        Packet packet2 = consume(node2);
        assertEquals("me", packet1.sender.value);
        assertEquals("me", packet2.sender.value);
        assertEquals("room", packet1.topic.value);
        assertEquals("room", packet2.topic.value);
        assertEquals("on", packet1.message);
        assertEquals("on", packet2.message);
    }

    private static Packet packet(String sender, String topic, String message) {
        return Packet.builder()
                .sender(new Packet.Sender(sender))
                .topic(new Packet.Topic(topic))
                .message(message)
                .build();
    }

}
