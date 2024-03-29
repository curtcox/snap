package com.curtcox.snap.model;

import org.junit.Test;

import static org.junit.Assert.*;
import com.curtcox.snap.model.Packet.*;

import java.io.*;

public class PacketTest {

    Trigger t0 = Trigger.from(0);
    Trigger t1 = Trigger.from(1);

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

    private Packet packet(String sender, String topic, String message, long timestamp, Trigger trigger) {
        return Packet.builder()
                .sender(new Sender(sender))
                .topic(new Topic(topic))
                .message(message)
                .timestamp(new Timestamp(timestamp))
                .trigger(trigger)
                .build();
    }

    @Test
    public void packets_are_equivalent_after_being_converted_to_and_from_bytes() throws IOException {
        for (int i=0; i<10; i++) {
            Packet packet = Random.packet();
            Packet copy = Packet.from(packet.asBytes());
            assertEquals(packet,copy);
            assertEquals(copy,packet);
        }
    }

    @Test
    public void packets_are_always_converted_to_the_same_bytes() {
        for (int i=0; i<10; i++) {
            Packet packet = Random.packet();
            assertEquals(packet.asBytes(),packet.asBytes());
        }
    }
}
