package com.curtcox;

import org.junit.Test;

import static org.junit.Assert.*;

public class TopicPacketFilterTest {

    @Test
    public void can_create() {
        assertNotNull(new TopicPacketFilter(""));
    }

    @Test
    public void passes_exact_matches() {
        TopicPacketFilter filter = new TopicPacketFilter("moon");
        assertTrue(filter.passes(new Packet("moon","")));
        assertFalse(filter.passes(new Packet("pre-moon","")));
        assertFalse(filter.passes(new Packet("moon-post","")));
        assertFalse(filter.passes(new Packet("moom","")));
        assertFalse(filter.passes(new Packet("oo","")));
        assertFalse(filter.passes(new Packet("","")));
    }

}
