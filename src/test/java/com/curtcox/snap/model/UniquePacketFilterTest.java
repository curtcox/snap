package com.curtcox.snap.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class UniquePacketFilterTest {

    UniquePacketFilter filter = new UniquePacketFilter();

    @Test
    public void unique_packets_always_pass_filter() {
        for (int i=0; i<1000; i++) {
            assertTrue(filter.passes(Random.packet()));
        }
    }

    @Test
    public void immediate_duplicate_is_ignored() {
        Packet packet = Random.packet();
        assertTrue(filter.passes(packet));
        assertFalse(filter.passes(packet));
        assertFalse(filter.passes(packet));
    }

    @Test
    public void unique_is_passed_when_among_duplicates() {
        Packet packet = Random.packet();
        assertTrue(filter.passes(packet));
        assertFalse(filter.passes(packet));
        assertTrue(filter.passes(Random.packet()));
        assertTrue(filter.passes(Random.packet()));
        assertFalse(filter.passes(packet));
        assertTrue(filter.passes(Random.packet()));
    }

}
