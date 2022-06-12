package com.curtcox;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static com.curtcox.Random.random;
import static com.curtcox.TestUtil.shortDelay;
import static org.junit.Assert.*;

public class NodeTest {

    Node node;

    ReflectorNetwork network = new ReflectorNetwork();

    @Before
    public void setUp() {
        node = Node.on(network);
    }

    @After
    public void tearDown() {
        network.shutdown();
    }

    @Test
    public void write_should_not_produce_error() {
        node.write(new Packet("schmopic","smessage"));
    }

    @Test
    public void read_should_return_the_message_sent() {
        String topic = random("topic");
        String message = random("message");

        node.write(new Packet(topic,message));
        shortDelay();
        Packet packet = node.read(topic);

        assertNotNull(packet);
        assertEquals(topic,packet.topic);
        assertEquals(message,packet.message);
    }

    @Test
    public void read_should_only_return_a_message_once_when_topic_specified() {
        String topic = random("topic");
        node.write(new Packet(topic,random("message")));
        shortDelay();
        assertNotNull(node.read(topic));
        assertNull(node.read(topic));
    }

    @Test
    public void read_should_return_a_message_once_when_no_topic_specified() {
        node.write(new Packet(random("topic"),random("message")));
        shortDelay();

        assertNotNull(node.read());
        assertNull(node.read());
    }

    @Test
    public void read_should_return_null_when_no_messages_sent() {
        shortDelay();
        Packet packet = node.read();

        assertNull(packet);
    }

    @Test
    public void read_should_return_null_when_there_is_a_message_that_does_not_match_topic() {
        String topic = random("topic");
        String message = random("message");

        node.write(new Packet(topic,message));
        shortDelay();
        Packet packet = node.read("different " + topic);

        assertNull(packet);
    }

    @Test
    public void read_with_no_topic_should_return_messages_sent_to_any_topic() {
        String topic1 = random("topic1");
        String message1 = random("message1");
        node.write(new Packet(topic1,message1));
        String topic2 = random("topic2");
        String message2 = random("message2");
        node.write(new Packet(topic2,message2));
        shortDelay();

        Packet packet1 = node.read();

        assertNotNull(packet1);
        assertEquals(topic1,packet1.topic);
        assertEquals(message1,packet1.message);

        Packet packet2 = node.read();

        assertNotNull(packet2);
        assertEquals(topic2,packet2.topic);
        assertEquals(message2,packet2.message);
    }

    @Test
    public void read_with_topic_should_return_1st_message_when_it_matches_topic() {
        String topic1 = random("topic1");
        String message1 = random("message1");
        node.write(new Packet(topic1,message1));
        String topic2 = random("topic2");
        String message2 = random("message2");
        node.write(new Packet(topic2,message2));
        shortDelay();

        Packet packet = node.read(topic1);

        assertNotNull(packet);
        assertEquals(topic1,packet.topic);
        assertEquals(message1,packet.message);

        assertNull(node.read(topic1));
    }

    @Test
    public void read_with_topic_should_return_2nd_message_when_it_matches_topic() {
        String topic1 = random("topic1");
        String message1 = random("message1");
        node.write(new Packet(topic1,message1));
        String topic2 = random("topic2");
        String message2 = random("message2");
        node.write(new Packet(topic2,message2));
        shortDelay();

        Packet packet = node.read(topic2);

        assertNotNull(packet);
        assertEquals(topic2,packet.topic);
        assertEquals(message2,packet.message);
        assertNull(node.read(topic2));
    }

    @Test
    public void messsages_are_delivered_in_order_when_on_network() {
        Node.on(network);

        node.write(new Packet("call","1"));
        node.write(new Packet("call","2"));
        node.write(new Packet("call","3"));
        node.write(new Packet("call","4"));
        shortDelay();

        assertEquals("1", node.read().message);
        assertEquals("2", node.read().message);
        assertEquals("3", node.read().message);
        assertEquals("4", node.read().message);
    }

    @Test
    public void messages_can_be_read_only_once() {
        node.write(new Packet("phone","ring"));
        shortDelay();

        assertNotNull(node.read());
        assertNull(node.read());
    }

}
