package com.curtcox.snap.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class TopicPacketFilterTest {

    @Test
    public void can_create() {
        assertNotNull(new TopicPacketFilter(new Packet.Topic("")));
    }

    @Test
    public void passes_exact_matches() {
        TopicPacketFilter filter = new TopicPacketFilter(new Packet.Topic("moon"));
        assertTrue(filter.passes(packet("","moon","")));
        assertFalse(filter.passes(packet("","pre-moon","")));
        assertFalse(filter.passes(packet("","moon-post","")));
        assertFalse(filter.passes(packet("","moom","")));
        assertFalse(filter.passes(packet("","oo","")));
        assertFalse(filter.passes(packet("","","")));
    }

    private static Packet packet(String sender, String topic, String message) {
        return Packet.builder()
                .sender(new Packet.Sender(sender))
                .topic(new Packet.Topic(topic))
                .message(message)
                .build();
    }

}
