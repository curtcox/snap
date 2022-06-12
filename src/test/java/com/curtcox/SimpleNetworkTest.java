package com.curtcox;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import static com.curtcox.TestUtil.shortDelay;
import static org.junit.Assert.*;

public class SimpleNetworkTest {

    SimpleNetwork network = SimpleNetwork.newPolling();
    Node node1 = Node.on(network);
    Node node2 = Node.on(network);

    IO io1 = new IO();
    IO io2 = new IO();

    @Rule
    public Timeout globalTimeout = Timeout.seconds(2);

    @Test
    public void can_create() {
        assertNotNull(SimpleNetwork.newPolling());
    }

    @Test
    public void there_are_no_messages_to_listen_to_before_any_are_sent() {
        assertNull(node1.read());
        assertNull(node2.read());
    }

    @Test
    public void messages_can_be_sent_over_a_network() {
        Packet packet = Random.packet();
        node1.write(packet);
        shortDelay();

        Packet read = node2.read();
        assertEquals(packet,read);
    }

    @Test
    public void messages_are_delivered_in_order_over_network() {
        node1.write(new Packet("call","Mom"));
        node1.write(new Packet("call","Dad"));
        shortDelay();

        assertEquals("Mom", node2.read().message);
        assertEquals("Dad", node2.read().message);
    }

    @Test
    public void messages_can_be_read_only_once_by_receiver() {
        node1.write(new Packet("phone","ring"));
        shortDelay();

        assertNotNull(node2.read());
        assertNull(node2.read());
    }

    @Test
    public void messages_can_be_read_at_multiple_points() {
        Node node3 = Node.on(network);
        node1.write(new Packet("phone","ring"));
        shortDelay();

        Packet packet2 = node2.read();
        assertEquals("phone",packet2.topic);
        assertEquals("ring",packet2.message);

        Packet packet3 = node3.read();
        assertEquals("phone",packet3.topic);
        assertEquals("ring",packet3.message);
    }

    class IO implements Packet.IO {

        Packet toRead;
        Packet written;
        boolean wrote;
        @Override
        public Packet read() {
            return toRead;
        }

        @Override
        public void write(Packet packet) {
            written = packet;
            wrote = true;
        }
    }

    @Test
    public void no_packets_are_written_to_IO_when_there_are_no_packets_to_read() {
        network.add(io1);
        network.add(io2);

        assertFalse(io1.wrote);
        assertNull(io1.written);
        assertFalse(io2.wrote);
        assertNull(io2.written);
    }

    @Test
    public void packet_is_written_to_IO_when_there_is_a_packet_to_read() {
        Packet packet = new Packet("room","on");
        io1.toRead = packet;

        network.add(io1);
        network.add(io2);

        shortDelay();
        assertEquals(packet,io2.written);
    }

    @Test
    public void packet_is_written_to_IO_even_if_no_packet_is_initially_available() {
        network.add(io1);
        network.add(io2);

        Packet packet = new Packet("room","on");
        io1.toRead = packet;

        shortDelay();
        assertEquals(packet,io2.written);
    }

    @Test
    public void messages_from_IO_are_delived_to_nodes() {
        Packet packet = new Packet("room","on");
        io1.toRead = packet;

        network.add(io1);
        network.add(io2);

        shortDelay();
        Packet packet1 = node1.read();
        Packet packet2 = node2.read();
        assertEquals("room", packet1.topic);
        assertEquals("room", packet2.topic);
        assertEquals("on", packet1.message);
        assertEquals("on", packet2.message);
    }

}
