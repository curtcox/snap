package com.curtcox;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import static org.junit.Assert.*;

public class NetworkTest {

    Network network = new Network();
    Node node1 = Node.on(network);
    Node node2 = Node.on(network);

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

}