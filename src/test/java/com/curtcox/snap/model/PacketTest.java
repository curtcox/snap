package com.curtcox.snap.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class PacketTest {

    Packet.Trigger t0 = Packet.Trigger.from(0);
    Packet.Trigger t1 = Packet.Trigger.from(1);

    @Test
    public void equal() {
        assertEqualPackets(packet("","","",0,t0),packet("","","",0,t0));
        assertEqualPackets(packet("s","","",0,t0),packet("s","","",0,t0));
        assertEqualPackets(packet("","t","",0,t0),packet("","t","",0,t0));
        assertEqualPackets(packet("","","m",0,t0),packet("","","m",0,t0));
        assertEqualPackets(packet("","","",1,t0),packet("","","",1,t0));
        assertEqualPackets(packet("","","",0,t1),packet("","","",0,t1));
    }

    @Test
    public void unequal() {
        assertUnequalPackets(packet("","","",0,t1),packet("","","",0,t0));
        assertUnequalPackets(packet("","","",1,t0),packet("","","",0,t0));
        assertUnequalPackets(packet("","","x",0,t0),packet("","","",0,t0));
        assertUnequalPackets(packet("","x","",0,t0),packet("","","",0,t0));
        assertUnequalPackets(packet("x","","",0,t0),packet("","","",0,t0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructor_throws_IllegalArgumentException_when_packet_is_too_big() {
        String sender = "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";
        String topic = sender + sender + sender + sender + sender + sender;
        String message = sender + topic + sender + topic + sender + topic;
        String text = sender + topic + message;
        assertTrue(text.length()>Packet.MAX_SIZE);
        new Packet(new Packet.Sender(sender),new Packet.Topic(topic),message);
    }

    private void assertEqualPackets(Packet a, Packet b) {
        assertEquals(a,b);
        assertEquals(b,a);
        assertEquals(a.hashCode(),b.hashCode());
    }

    private void assertUnequalPackets(Packet a, Packet b) {
        assertNotEquals(a,b);
        assertNotEquals(b,a);
        assertNotEquals(a.hashCode(),b.hashCode());
    }

    private Packet packet(String sender, String topic, String message, long timestamp, Packet.Trigger trigger) {
        return Packet.builder()
                .sender(new Packet.Sender(sender))
                .topic(new Packet.Topic(topic))
                .message(message)
                .timestamp(timestamp)
                .trigger(trigger)
                .build();
    }

}
