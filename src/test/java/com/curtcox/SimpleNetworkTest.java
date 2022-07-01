package com.curtcox;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.io.IOException;
import java.util.Iterator;

import static com.curtcox.TestUtil.consume;
import static com.curtcox.TestUtil.tick;
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
        assertNull(node1.read().read());
        assertNull(node2.read().read());
    }

    @Test
    public void messages_can_be_sent_over_a_network() throws IOException {
        Packet packet = Random.packet();
        node1.write(packet);
        tick(2);

        Packet read = node2.read().read();
        assertEquals(packet,read);
    }

    @Test
    public void messages_are_delivered_in_order_over_network() throws IOException {
        node1.write(new Packet("call","Mom"));
        node1.write(new Packet("call","Dad"));
        tick(3);
        Packet.Reader packets = node2.read();

        assertEquals("Mom", packets.read().message);
        assertEquals("Dad", packets.read().message);
    }

    @Test
    public void messages_can_be_read_only_once_by_receiver() throws IOException {
        node1.write(new Packet("phone","ring"));
        tick(2);

        assertNotNull(consume(node2));
        assertNull(node2.read().read());
    }

    @Test
    public void messages_can_be_read_at_multiple_points() throws IOException {
        Node node3 = Node.on(network);
        node1.write(new Packet("phone","ring"));
        tick(2);

        Packet packet2 = node2.read().read();
        assertEquals("phone",packet2.topic);
        assertEquals("ring",packet2.message);

        Packet packet3 = node3.read().read();
        assertEquals("phone",packet3.topic);
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
        Packet packet = new Packet("room","on");
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

        Packet packet = new Packet("room","on");
        io1.add(packet);

        tick(2);
        assertEquals(1,io2.written().size());
        assertEquals(packet,io2.written().get(0));
    }

    @Test
    public void messages_from_IO_are_delived_to_nodes() throws IOException {
        Packet packet = new Packet("room","on");
        io1.add(packet);

        network.add(io1);
        network.add(io2);

        tick(2);

        Packet packet1 = consume(node1);
        Packet packet2 = consume(node2);
        assertEquals("room", packet1.topic);
        assertEquals("room", packet2.topic);
        assertEquals("on", packet1.message);
        assertEquals("on", packet2.message);
    }

}
