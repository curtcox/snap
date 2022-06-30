package com.curtcox;

import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;

import static com.curtcox.Random.random;
import static com.curtcox.TestUtil.consume;
import static com.curtcox.TestUtil.tick;
import static org.junit.Assert.*;

public class NodeTest {

    Node node;

    ReflectorNetwork network = new ReflectorNetwork();

    @Before
    public void setUp() {
        node = Node.on(network);
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
        tick(2);
        Packet packet = node.read(topic).next();

        assertNotNull(packet);
        assertEquals(topic,packet.topic);
        assertEquals(message,packet.message);
    }

    @Test
    public void read_should_only_return_a_message_once_when_topic_specified() {
        String topic = random("topic");
        node.write(new Packet(topic,random("message")));
        tick(2);
        assertNotNull(node.read(topic));
        assertNull(node.read(topic));
    }

    @Test
    public void read_should_return_a_message_once_when_no_topic_specified() {
        node.write(new Packet(random("topic"),random("message")));
        tick(2);

        Iterator<Packet> iterator = node.read();
        assertNotNull(iterator.next());
        iterator.remove();
        assertFalse(iterator.hasNext());
    }

    @Test
    public void read_should_return_empty_iterator_when_no_messages_sent() {
        tick();
        Iterator<Packet> packets = node.read();

        assertFalse(packets.hasNext());
    }

    @Test
    public void read_should_return_null_when_there_is_a_message_that_does_not_match_topic() {
        String topic = random("topic");
        String message = random("message");

        node.write(new Packet(topic,message));
        tick();
        Packet packet = node.read("different " + topic).next();

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
        tick(3);

        Packet packet1 = consume(node);

        assertNotNull(packet1);
        assertEquals(topic1,packet1.topic);
        assertEquals(message1,packet1.message);

        Packet packet2 = consume(node);

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
        tick(2);

        Packet packet = node.read(topic1).next();

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
        tick(3);

        Packet packet = node.read(topic2).next();

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
        tick(5);

        assertEquals("1", consume(node).message);
        assertEquals("2", consume(node).message);
        assertEquals("3", consume(node).message);
        assertEquals("4", consume(node).message);
    }

    @Test
    public void messages_can_be_read_only_once() {
        node.write(new Packet("phone","ring"));
        tick(2);

        assertNotNull(consume(node));
        assertFalse(node.read().hasNext());
    }

}
