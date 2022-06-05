package com.curtcox;

import org.junit.Before;
import org.junit.Test;

import static com.curtcox.Random.random;
import static org.junit.Assert.*;

public class SnapTest {

    Snap snap;

    Node node;
    Network network = Network.newPolling();

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
        Packet packet = snap.listen(topic);

        assertNotNull(packet);
        assertEquals(topic,packet.topic);
        assertEquals(message,packet.message);
    }

    @Test
    public void read_should_only_return_a_message_once_when_topic_specified() {
        String topic = random("topic");
        snap.send(topic,random("message"));
        assertNotNull(snap.listen(topic));
        assertNull(snap.listen(topic));
    }

    @Test
    public void read_should_only_return_a_message_once_when_no_topic_specified() {
        snap.send(random("topic"),random("message"));
        assertNotNull(snap.listen());
        assertNull(snap.listen());
    }

    @Test
    public void read_should_return_null_when_no_messages_sent() {
        Packet packet = snap.listen();

        assertNull(packet);
    }

    @Test
    public void read_should_return_null_when_there_is_a_message_that_does_not_match_topic() {
        String topic = random("topic");
        String message = random("message");

        snap.send(topic,message);
        Packet packet = snap.listen("different " + topic);

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

        Packet packet1 = snap.listen();

        assertNotNull(packet1);
        assertEquals(topic1,packet1.topic);
        assertEquals(message1,packet1.message);

        Packet packet2 = snap.listen();

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

        Packet packet = snap.listen(topic1);

        assertNotNull(packet);
        assertEquals(topic1,packet.topic);
        assertEquals(message1,packet.message);

        assertNull(snap.listen(topic1));
    }

    @Test
    public void read_with_topic_should_return_2nd_message_when_it_matches_topic() {
        String topic1 = random("topic1");
        String message1 = random("message1");
        snap.send(topic1,message1);
        String topic2 = random("topic2");
        String message2 = random("message2");
        snap.send(topic2,message2);

        Packet packet = snap.listen(topic2);

        assertNotNull(packet);
        assertEquals(topic2,packet.topic);
        assertEquals(message2,packet.message);
        assertNull(snap.listen(topic2));
    }

    @Test
    public void messsages_are_delivered_in_order_when_on_network() {
        Node.on(network);

        snap.send("call","1");
        snap.send("call","2");
        snap.send("call","3");
        snap.send("call","4");

        assertEquals("1", snap.listen().message);
        assertEquals("2", snap.listen().message);
        assertEquals("3", snap.listen().message);
        assertEquals("4", snap.listen().message);
    }

}
