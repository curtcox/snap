package com.curtcox.snap.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class PacketTest {

    @Test
    public void equal() {
        assertEqualPackets(new Packet("","",""),new Packet("","",""));
        assertEqualPackets(new Packet("s","",""),new Packet("s","",""));
        assertEqualPackets(new Packet("","t",""),new Packet("","t",""));
        assertEqualPackets(new Packet("","","m"),new Packet("","","m"));
    }

    @Test
    public void unequal() {
        assertUnequalPackets(new Packet("","","x"),new Packet("","",""));
        assertUnequalPackets(new Packet("","x",""),new Packet("","",""));
        assertUnequalPackets(new Packet("x","",""),new Packet("","",""));
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
