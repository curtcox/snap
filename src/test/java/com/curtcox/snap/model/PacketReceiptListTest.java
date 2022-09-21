package com.curtcox.snap.model;

import org.junit.Test;

import java.util.Iterator;

import static org.junit.Assert.*;

public class PacketReceiptListTest {

    final PacketReceiptList list = new PacketReceiptList();

    @Test
    public void empty_list() {
        assertTrue(list.isEmpty());
        assertEquals(0,list.size());
        assertFalse(list.iterator().hasNext());
    }

    @Test
    public void list_with_one_element() {
        Packet packet = Random.packet();
        list.add(packet);
        assertFalse(list.isEmpty());
        assertEquals(1,list.size());
        assertTrue(list.iterator().hasNext());

        Iterator<Packet.Receipt> iterator = list.iterator();
        assertEquals(packet,iterator.next().packet);
        assertFalse(iterator.hasNext());
    }

    @Test
    public void toString_contains_packet_string() {
        Packet packet = Random.packet();
        list.add(packet);
        assertContains(list.toString(),packet.toString());
    }

    private void assertContains(String whole, String part) {
        assertTrue(whole + " should contain " + part, whole.contains(part));
    }


}
