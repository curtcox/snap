package com.curtcox;

import org.junit.Test;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.Assert.*;

public class PacketListTest {

    PacketList list = new PacketList();

    Packet packet = new Packet("1","1");
    Packet packet2 = new Packet("2","2");

    @Test
    public void can_create() {
        assertNotNull(new PacketList());
    }

    @Test
    public void take_is_null_when_list_is_empty() {
        assertNull(list.take());
    }

    @Test
    public void take_is_null_after_only_packet_is_taken() {
        list.add(packet);
        assertEquals(packet,list.take());
        assertNull(list.take());
    }

    @Test
    public void take_is_null_after_both_packets_are_taken() {
        list.add(packet);
        list.add(packet2);
        assertEquals(packet,list.take());
        assertEquals(packet2,list.take());
        assertNull(list.take());
    }

    @Test
    public void iterator_with_no_packets_hasNext_is_false() {
        Iterator<Packet> iterator = list.read();

        assertFalse(iterator.hasNext());
    }

    @Test(expected = NoSuchElementException.class)
    public void iterator_with_no_packets_next_throws_exception() {
        list.read().next();
    }

    @Test
    public void iterator_with_one_packet() {
        list.add(packet);

        Iterator<Packet> iterator = list.read();

        assertTrue(iterator.hasNext());
        assertEquals(packet,iterator.next());
    }

    @Test
    public void reading_iteration_with_one_packet_does_not_remove_packet_from_the_list() {
        list.add(packet);

        list.read().next();
        Iterator<Packet> iterator = list.read();

        assertTrue(iterator.hasNext());
        assertEquals(packet,iterator.next());
    }

    @Test
    public void taking_one_packet_removes_it_from_the_list() {
        list.add(packet);

        Iterator<Packet> iterator = list.read();

        assertEquals(packet,list.take());
        assertFalse(iterator.hasNext());
        assertFalse(list.read().hasNext());
    }

    @Test
    public void taking_two_packets_removes_them_from_the_list() {
        list.add(packet);
        list.add(packet2);

        Iterator<Packet> iterator = list.read();

        assertEquals(packet,list.take());
        assertEquals(packet2,list.take());
        assertFalse(iterator.hasNext());
        assertFalse(list.read().hasNext());
    }

    @Test
    public void iterator_with_two_packets() {
        list.add(packet);
        list.add(packet2);

        Iterator<Packet> iterator = list.read();

        assertTrue(iterator.hasNext());
        assertEquals(packet,iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(packet2,iterator.next());
    }

    @Test
    public void iterator_with_one_packet_that_is_taken_before_it_is_read_indicates_no_next() {
        list.add(packet);

        Iterator<Packet> iterator = list.read();

        assertEquals(packet,list.take());
        assertFalse(iterator.hasNext());
    }

    @Test(expected = NoSuchElementException.class)
    public void iterator_with_one_packet_that_is_taken_before_it_is_read_throws_exception() {
        list.add(packet);
        Iterator<Packet> iterator = list.read();
        assertEquals(packet,list.take());
        iterator.next();
    }

    @Test
    public void iterator_with_two_packets_where_one_is_taken_before_it_is_read() {
        list.add(packet);
        list.add(packet2);

        Iterator<Packet> iterator = list.read();
        assertTrue(iterator.hasNext());
        assertEquals(packet,iterator.next());
        assertEquals(packet,list.take());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void iterator_with_two_packets_where_one_is_taken_before_it_is_read2() {
        list.add(packet);
        list.add(packet2);

        assertEquals(packet,list.take());
        assertEquals(packet2,list.take());
    }

    @Test
    public void iterator_with_two_packets_where_both_are_taken_before_they_are_read() {
        list.add(packet);
        list.add(packet2);

        Iterator<Packet> iterator = list.read();
        assertEquals(packet,list.take());
        assertEquals(packet2,list.take());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void empty_iterator_returns_packet_added_after_creation() {
        Iterator<Packet> iterator = list.read();
        list.add(packet);
        assertTrue(iterator.hasNext());
        assertEquals(packet,iterator.next());
    }

    @Test
    public void iterator_with_packet_returns_packet_added_after_creation() {
        list.add(packet);
        Iterator<Packet> iterator = list.read();
        list.add(packet2);
        assertTrue(iterator.hasNext());
        assertEquals(packet,iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(packet2,iterator.next());
    }

}
