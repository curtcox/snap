package com.curtcox;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.io.IOException;

import static org.junit.Assert.*;

public class NetworkTest {

    Network network = new Network();
    Node node1 = Node.on(network);
    Node node2 = Node.on(network);

    IO io = new IO();

    @Rule
    public Timeout globalTimeout = Timeout.seconds(2);

    @Test
    public void can_create() {
        assertNotNull(new Network());
    }

    @Test
    public void there_are_no_messages_to_listen_to_before_any_are_sent() {
        assertNull(node1.listen());
        assertNull(node2.listen());
    }

    @Test
    public void messages_can_be_sent_over_a_network() {
        node1.send("phone","ring");
        Packet packet = node2.listen();
        assertEquals("phone",packet.topic);
        assertEquals("ring",packet.message);
    }

    @Test
    public void messages_are_delivered_in_order_locally() {
        node1.send("call","Mom");
        node1.send("call","Dad");

        assertEquals("Mom", node1.listen().message);
        assertEquals("Dad", node1.listen().message);
    }

    @Test
    public void messages_are_delivered_in_order_over_network() {
        node1.send("call","Mom");
        node1.send("call","Dad");

        assertEquals("Mom", node2.listen().message);
        assertEquals("Dad", node2.listen().message);
    }

    @Test
    public void messages_can_be_read_only_once_at_each_point() {
        node1.send("phone","ring");

        assertNotNull(node1.listen());
        assertNotNull(node2.listen());
        assertNull(node1.listen());
        assertNull(node2.listen());
    }

    @Test
    public void messages_can_be_read_at_multiple_points() {
        node1.send("phone","ring");

        Packet packet1 = node1.listen();
        assertEquals("phone",packet1.topic);
        assertEquals("ring",packet1.message);

        Packet packet2 = node2.listen();
        assertEquals("phone",packet2.topic);
        assertEquals("ring",packet2.message);
    }

    class IO implements Packet.IO {

        Packet toRead;
        Packet written;
        boolean wrote;
        @Override
        public Packet read() throws IOException {
            return toRead;
        }

        @Override
        public void write(Packet packet) throws IOException {
            written = packet;
            wrote = true;
        }
    }

    @Test
    public void no_packets_are_written_to_IO_when_there_are_no_packets_to_read() {
        network.add(io);

        assertFalse(io.wrote);
        assertNull(io.written);
    }

    @Test
    public void packet_is_written_to_IO_when_there_is_a_packets_to_read() {
        Packet packet = new Packet("room","on");
        io.toRead = packet;

        network.add(io);

        assertEquals(packet,io.written);
    }

    @Test
    public void messages_from_IO_are_delived_to_nodes() {
        Packet packet = new Packet("room","on");
        io.toRead = packet;

        network.add(io);

        Packet packet1 = node1.listen();
        Packet packet2 = node2.listen();
        assertEquals("room", packet1.topic);
        assertEquals("room", packet2.topic);
        assertEquals("on", packet1.message);
        assertEquals("on", packet2.message);
    }

}
