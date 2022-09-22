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

    @Test
    public void filtered_returns_list_of_items_that_passes_filter() {
        Packet.Topic topic1 = Random.topic();
        Packet.Topic topic2 = Random.topic();
        Packet.Builder builder = Packet.builder()
                .sender(Random.sender())
                .message("");
        int count = 10;
        for (int i=0; i<count; i++) {
            list.add(builder.topic(topic1).build());
            list.add(builder.topic(topic2).build());
        }
        PacketReceiptList all1 = list.filter(packet -> packet.topic.equals(topic1));
        PacketReceiptList all2 = list.filter(packet -> packet.topic.equals(topic2));
        assertEquals(count,all1.size());
        assertEquals(count,all2.size());
    }

    private void assertContains(String whole, String part) {
        assertTrue(whole + " should contain " + part, whole.contains(part));
    }


}
