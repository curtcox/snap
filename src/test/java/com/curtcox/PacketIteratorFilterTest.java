package com.curtcox;

import org.junit.Test;

import java.util.Arrays;
import java.util.Iterator;

import static org.junit.Assert.*;

public class PacketIteratorFilterTest {

    @Test
    public void can_create() {
        assertNotNull(new PacketIteratorFilter(iterator(),filter("")));
    }

    @Test
    public void empty() {
        PacketIteratorFilter filter = new PacketIteratorFilter(iterator(), filter(""));
        assertFalse(filter.hasNext());
        assertNull(filter.next());
    }

    @Test
    public void one_not_matching() {
        PacketIteratorFilter filter = new PacketIteratorFilter(iterator("this"), filter("that"));
        assertFalse(filter.hasNext());
        assertNull(filter.next());
    }

    @Test
    public void one_matching() {
        PacketIteratorFilter filter = new PacketIteratorFilter(iterator("one"), filter("one"));
        assertTrue(filter.hasNext());
        assertEquals("one",filter.next().topic);
        assertFalse(filter.hasNext());
        assertNull(filter.next());
    }

    private Packet.Filter filter(String text) {
        return packet -> packet.topic.contains(text);
    }

    private Iterator<Packet> iterator(String... topics) {
        return Arrays.stream(topics).map((t)->new Packet(t,"")).iterator();
    }
}
