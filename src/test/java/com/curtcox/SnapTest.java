package com.curtcox;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SnapTest {

    Snap snap;

    @Before
    public void setUp() {
        snap = new Snap();
    }

    @Test
    public void send_should_not_produce_error() {
        snap.send("schmopic","smessage");
    }

    @Test
    public void listen_should_return_the_message_sent() {
        String topic = random("topic");
        String message = random("message");

        snap.send(topic,message);
        Packet packet = snap.listen(topic);

        assertNotNull(packet);
        assertEquals(topic,packet.topic);
        assertEquals(message,packet.message);
    }

    @Test
    public void listen_should_return_null_when_no_messages_sent() {
        Packet packet = snap.listen();

        assertNull(packet);
    }

    @Test
    public void listen_should_return_null_when_there_is_a_message_that_does_not_match_topic() {
        String topic = random("topic");
        String message = random("message");

        snap.send(topic,message);
        Packet packet = snap.listen("different " + topic);

        assertNull(packet);
    }

    @Test
    public void listen_with_no_topic_should_return_messages_sent_to_any_topic() {
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
    public void listen_with_topic_should_return_1st_message_when_it_matches_topic() {
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
    public void listen_with_topic_should_return_2nd_message_when_it_matches_topic() {
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

    String random(String prefix) {
        return prefix + System.currentTimeMillis() % 1000;
    }

}
