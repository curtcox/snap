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
        assertNull(filter.read());
//        assertNull(filter.next());
    }

    @Test
    public void one_not_matching() {
        PacketIteratorFilter filter = new PacketIteratorFilter(iterator("this"), filter("that"));
        assertNull(filter.read());
//        assertNull(filter.next());
    }

    @Test
    public void one_matching() {
        PacketIteratorFilter filter = new PacketIteratorFilter(iterator("one"), filter("one"));
        //assertTrue(filter.hasNext());
        assertEquals("one",filter.read().topic);
        //assertFalse(filter.hasNext());
        assertNull(filter.read());
    }

    private Packet.Filter filter(String text) {
        return packet -> packet.topic.contains(text);
    }

    private Packet.Reader iterator(String... topics) {
        FakeIO io = new FakeIO();
        for (String topic : topics) {
            io.add(new Packet(topic,""));
        }
        return io;
    }
}
