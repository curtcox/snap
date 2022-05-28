package com.curtcox;

import org.junit.Test;

import static org.junit.Assert.*;

public class NetworkTest {

    Network network = new Network();
    Node snap1 = Node.on(network);
    Node snap2 = Node.on(network);

    @Test
    public void can_create() {
        assertNotNull(new Network());
    }

    @Test
    public void there_are_no_messages_to_listen_to_before_any_are_sent() {
        assertNull(snap1.listen());
        assertNull(snap2.listen());
    }

    @Test
    public void messsages_can_be_sent_over_a_network() {
        snap1.send("phone","ring");
        Packet packet = snap2.listen();
        assertEquals("phone",packet.topic);
        assertEquals("ring",packet.message);
    }

    @Test
    public void messsages_are_delivered_in_order_locally() {
        snap1.send("call","Mom");
        snap1.send("call","Dad");

        assertEquals("Mom",snap1.listen().message);
        assertEquals("Dad",snap1.listen().message);
    }

    @Test
    public void messsages_are_delivered_in_order_over_network() {
        snap1.send("call","Mom");
        snap1.send("call","Dad");

        assertEquals("Mom",snap2.listen().message);
        assertEquals("Dad",snap2.listen().message);
    }

    @Test
    public void messsages_can_be_read_only_once_at_each_point() {
        snap1.send("phone","ring");

        assertNotNull(snap1.listen());
        assertNotNull(snap2.listen());
        assertNull(snap1.listen());
        assertNull(snap2.listen());
    }

    @Test
    public void messsages_can_be_read_at_multiple_points() {
        snap1.send("phone","ring");

        Packet packet1 = snap1.listen();
        assertEquals("phone",packet1.topic);
        assertEquals("ring",packet1.message);

        Packet packet2 = snap2.listen();
        assertEquals("phone",packet2.topic);
        assertEquals("ring",packet2.message);
    }

}
