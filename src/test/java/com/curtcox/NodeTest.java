package com.curtcox;

import org.junit.Before;
import org.junit.Test;

import static com.curtcox.Random.random;
import static org.junit.Assert.*;

public class NodeTest {

    Node node;
    Network network = Network.newPolling();

    @Before
    public void setUp() {
        node = Node.on(network);
    }

    @Test
    public void send_should_not_produce_error() {
        node.send("schmopic","smessage");
    }

    @Test
    public void listen_should_return_the_message_sent() {
        String topic = random("topic");
        String message = random("message");

        node.send(topic,message);
        Packet packet = node.listen(topic);

        assertNotNull(packet);
        assertEquals(topic,packet.topic);
        assertEquals(message,packet.message);
    }

    @Test
    public void listen_should_only_return_a_message_once_when_topic_specified() {
        String topic = random("topic");
        node.send(topic,random("message"));
        assertNotNull(node.listen(topic));
        assertNull(node.listen(topic));
    }

    @Test
    public void listen_should_only_return_a_message_once_when_no_topic_specified() {
        node.send(random("topic"),random("message"));
        assertNotNull(node.listen());
        assertNull(node.listen());
    }

    @Test
    public void listen_should_return_null_when_no_messages_sent() {
        Packet packet = node.listen();

        assertNull(packet);
    }

    @Test
    public void listen_should_return_null_when_there_is_a_message_that_does_not_match_topic() {
        String topic = random("topic");
        String message = random("message");

        node.send(topic,message);
        Packet packet = node.listen("different " + topic);

        assertNull(packet);
    }

    @Test
    public void listen_with_no_topic_should_return_messages_sent_to_any_topic() {
        String topic1 = random("topic1");
        String message1 = random("message1");
        node.send(topic1,message1);
        String topic2 = random("topic2");
        String message2 = random("message2");
        node.send(topic2,message2);

        Packet packet1 = node.listen();

        assertNotNull(packet1);
        assertEquals(topic1,packet1.topic);
        assertEquals(message1,packet1.message);

        Packet packet2 = node.listen();

        assertNotNull(packet2);
        assertEquals(topic2,packet2.topic);
        assertEquals(message2,packet2.message);
    }

    @Test
    public void listen_with_topic_should_return_1st_message_when_it_matches_topic() {
        String topic1 = random("topic1");
        String message1 = random("message1");
        node.send(topic1,message1);
        String topic2 = random("topic2");
        String message2 = random("message2");
        node.send(topic2,message2);

        Packet packet = node.listen(topic1);

        assertNotNull(packet);
        assertEquals(topic1,packet.topic);
        assertEquals(message1,packet.message);

        assertNull(node.listen(topic1));
    }

    @Test
    public void listen_with_topic_should_return_2nd_message_when_it_matches_topic() {
        String topic1 = random("topic1");
        String message1 = random("message1");
        node.send(topic1,message1);
        String topic2 = random("topic2");
        String message2 = random("message2");
        node.send(topic2,message2);

        Packet packet = node.listen(topic2);

        assertNotNull(packet);
        assertEquals(topic2,packet.topic);
        assertEquals(message2,packet.message);
        assertNull(node.listen(topic2));
    }

    @Test
    public void messsages_are_delivered_in_order_when_on_network() {
        Node.on(network);

        node.send("call","1");
        node.send("call","2");
        node.send("call","3");
        node.send("call","4");

        assertEquals("1", node.listen().message);
        assertEquals("2", node.listen().message);
        assertEquals("3", node.listen().message);
        assertEquals("4", node.listen().message);
    }

}
