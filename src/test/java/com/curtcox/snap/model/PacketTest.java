package com.curtcox.snap.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class PacketTest {

    Packet.Trigger t0 = Packet.Trigger.from(0);
    Packet.Trigger t1 = Packet.Trigger.from(1);

    @Test
    public void equal() {
        assertEqualPackets(new Packet("","","",0,t0),new Packet("","","",0,t0));
        assertEqualPackets(new Packet("s","","",0,t0),new Packet("s","","",0,t0));
        assertEqualPackets(new Packet("","t","",0,t0),new Packet("","t","",0,t0));
        assertEqualPackets(new Packet("","","m",0,t0),new Packet("","","m",0,t0));
        assertEqualPackets(new Packet("","","",1,t0),new Packet("","","",1,t0));
        assertEqualPackets(new Packet("","","",0,t1),new Packet("","","",0,t1));
    }

    @Test
    public void unequal() {
        assertUnequalPackets(new Packet("","","",0,t1),new Packet("","","",0,t0));
        assertUnequalPackets(new Packet("","","",1,t0),new Packet("","","",0,t0));
        assertUnequalPackets(new Packet("","","x",0,t0),new Packet("","","",0,t0));
        assertUnequalPackets(new Packet("","x","",0,t0),new Packet("","","",0,t0));
        assertUnequalPackets(new Packet("x","","",0,t0),new Packet("","","",0,t0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructor_throws_IllegalArgumentException_when_packet_is_too_big() {
        String sender = "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";
        String topic = sender + sender + sender + sender + sender + sender;
        String message = sender + topic + sender + topic + sender + topic;
        String text = sender + topic + message;
        assertTrue(text.length()>Packet.MAX_SIZE);
        new Packet(sender,topic,message);
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
}
