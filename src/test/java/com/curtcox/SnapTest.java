package com.curtcox;

import org.junit.Test;

import static org.junit.Assert.*;

public class SnapTest {


    Snap snap = new Snap();

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

    String random(String prefix) {
        return prefix + System.currentTimeMillis() % 1000;
    }

}
