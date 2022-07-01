package com.curtcox;

import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;

import static com.curtcox.Random.random;
import static com.curtcox.TestUtil.consume;
import static com.curtcox.TestUtil.tick;
import static org.junit.Assert.*;

public class SnapTest {

    Snap snap;

    Node node;
    ReflectorNetwork network = new ReflectorNetwork();

    @Before
    public void setUp() {
        node = Node.on(network);
        snap = new Snap(node);
    }

    @Test(expected = NullPointerException.class)
    public void requires_node() {
        assertNotNull(new Snap(null));
    }

    @Test
    public void write_should_not_produce_error() {
        snap.send("schmopic","smessage");
    }

    @Test
    public void read_should_return_the_message_sent() {
        String topic = random("topic");
        String message = random("message");

        snap.send(topic,message);
        tick(2);
        Packet packet = snap.listen(topic).next();

        assertNotNull(packet);
        assertEquals(topic,packet.topic);
        assertEquals(message,packet.message);
    }

    @Test
    public void read_should_only_return_a_message_once_when_topic_specified() {
        String topic = random("topic");
        snap.send(topic,random("message"));
        tick(2);
        Iterator<Packet> iterator1 = snap.listen(topic);
        assertNotNull(iterator1.next());
        iterator1.remove();
        Iterator<Packet> iterator2 = snap.listen(topic);
        assertFalse(iterator2.hasNext());
    }

    @Test
    public void read_should_only_return_a_message_once_when_no_topic_specified() {
        snap.send(random("topic"),random("message"));
        tick(2);
        assertNotNull(consume(snap));
        assertFalse(snap.listen().hasNext());
    }

    @Test
    public void read_should_return_null_when_no_messages_sent() {
        assertFalse(snap.listen().hasNext());
    }

    @Test
    public void read_should_return_null_when_there_is_a_message_that_does_not_match_topic() {
        String topic = random("topic");
        String message = random("message");

        snap.send(topic,message);
        Packet packet = snap.listen("different " + topic).next();

        assertNull(packet);
    }

    @Test
    public void read_with_no_topic_should_return_messages_sent_to_any_topic() {
        String topic1 = random("topic1");
        String message1 = random("message1");
        snap.send(topic1,message1);
        String topic2 = random("topic2");
        String message2 = random("message2");
        snap.send(topic2,message2);
        tick(3);

        Iterator<Packet> packets = snap.listen();

        Packet packet1 = packets.next();

        assertNotNull(packet1);
        assertEquals(topic1,packet1.topic);
        assertEquals(message1,packet1.message);

        Packet packet2 = packets.next();

        assertNotNull(packet2);
        assertEquals(topic2,packet2.topic);
        assertEquals(message2,packet2.message);
    }

    @Test
    public void read_with_topic_should_return_1st_message_when_it_matches_topic() {
        String topic1 = random("topic1");
        String message1 = random("message1");
        snap.send(topic1,message1);
        String topic2 = random("topic2");
        String message2 = random("message2");
        snap.send(topic2,message2);
        tick(2);

        Iterator<Packet> iterator = snap.listen(topic1);
        Packet packet = iterator.next();
        iterator.remove();

        assertNotNull(packet);
        assertEquals(topic1,packet.topic);
        assertEquals(message1,packet.message);

        assertFalse(snap.listen(topic1).hasNext());
    }

    @Test
    public void read_with_topic_should_return_2nd_message_when_it_matches_topic() {
        String topic1 = random("topic1");
        String message1 = random("message1");
        snap.send(topic1,message1);
        String topic2 = random("topic2");
        String message2 = random("message2");
        snap.send(topic2,message2);
        tick(3);

        Iterator<Packet> iterator = snap.listen(topic2);
        Packet packet = iterator.next();
        iterator.remove();

        assertNotNull(packet);
        assertEquals(topic2,packet.topic);
        assertEquals(message2,packet.message);
        assertFalse(snap.listen(topic2).hasNext());
    }

    @Test
    public void messsages_are_delivered_in_order_when_on_network() {
        Node.on(network);

        snap.send("call","1");
        snap.send("call","2");
        snap.send("call","3");
        snap.send("call","4");
        tick(5);

        Iterator<Packet> packets = snap.listen();

        assertEquals("1", packets.next().message);
        assertEquals("2", packets.next().message);
        assertEquals("3", packets.next().message);
        assertEquals("4", packets.next().message);
    }

}
