package com.curtcox;

import org.junit.Test;

import java.io.IOException;
import java.util.Iterator;

import static org.junit.Assert.*;

public class PacketListTest {

    PacketList list = new PacketList();

    Packet packet = new Packet("one","1");
    Packet packet2 = new Packet("two","2");
    Packet packet3 = new Packet("three","3");

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
    public void take_is_null_after_only_packet_is_taken_via_iterator_remove() throws IOException {
        list.add(packet);
        Packet.Reader iterator = list.read();
        assertEquals(packet, iterator.read());
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
    public void take_is_null_after_3_packets_are_taken() {
        list.add(packet);
        list.add(packet2);
        list.add(packet3);
        assertEquals(packet,list.take());
        assertEquals(packet2,list.take());
        assertEquals(packet3,list.take());
        assertNull(list.take());
    }

    @Test
    public void take_is_null_after_both_packets_are_taken_via_iterator_remove() throws IOException {
        list.add(packet);
        list.add(packet2);
        Packet.Reader iterator = list.read();
        assertEquals(packet,iterator.read());
        assertEquals(packet2,iterator.read());
        assertNull(list.take());
    }

    @Test
    public void take_is_null_after_3_packets_are_taken_via_iterator_remove() throws IOException {
        list.add(packet);
        list.add(packet2);
        list.add(packet3);
        Packet.Reader iterator = list.read();
        assertEquals(packet,iterator.read());
        assertEquals(packet2,iterator.read());
        assertEquals(packet3,iterator.read());
        assertNull(list.take());
    }

    @Test
    public void iterator_with_no_packets_hasNext_is_false() throws IOException {
        Packet.Reader iterator = list.read();

        assertNull(iterator.read());
    }

    @Test
    public void iterator_with_no_packets_next_throws_exception() throws IOException {
        assertNull(list.read().read());
    }

    @Test
    public void iterator_with_one_packet() throws IOException {
        list.add(packet);

        Packet.Reader iterator = list.read();

        assertEquals(packet,iterator.read());
    }

    @Test
    public void reading_iteration_with_one_packet_does_not_remove_packet_from_the_list() throws IOException {
        list.add(packet);

        list.read().read();
        Packet.Reader iterator = list.read();

        assertEquals(packet,iterator.read());
    }

    @Test
    public void taking_one_packet_removes_it_from_the_list() throws IOException {
        list.add(packet);

        Packet.Reader iterator = list.read();

        assertEquals(packet,list.take());
        assertNull(list.read().read());
    }

    @Test
    public void taking_two_packets_removes_them_from_the_list() throws IOException {
        list.add(packet);
        list.add(packet2);

        Packet.Reader iterator = list.read();

        assertEquals(packet,list.take());
        assertEquals(packet2,list.take());
        assertNull(iterator.read());
        assertNull(list.read().read());
    }

    @Test
    public void iterator_with_two_packets() throws IOException {
        list.add(packet);
        list.add(packet2);

        Packet.Reader iterator = list.read();

        assertEquals(packet,iterator.read());
        assertEquals(packet2,iterator.read());
    }

    @Test
    public void iterator_with_one_packet_that_is_taken_before_it_is_read_indicates_no_next() throws IOException {
        list.add(packet);

        Packet.Reader iterator = list.read();

        assertEquals(packet,list.take());
        assertNull(iterator.read());
    }

    @Test
    public void iterator_with_one_packet_that_is_taken_before_it_is_read_throws_exception() throws IOException {
        list.add(packet);
        Packet.Reader iterator = list.read();
        assertEquals(packet,list.take());
        assertNull(iterator.read());
    }

    @Test
    public void iterator_with_two_packets_where_one_is_taken_before_it_is_read() throws IOException {
        list.add(packet);
        list.add(packet2);

        Packet.Reader iterator = list.read();
        assertEquals(packet,iterator.read());
        assertEquals(packet,list.take());
        assertNull(iterator.read());
    }

    @Test
    public void iterator_with_two_packets_where_one_is_taken_before_it_is_read2() {
        list.add(packet);
        list.add(packet2);

        assertEquals(packet,list.take());
        assertEquals(packet2,list.take());
    }

    @Test
    public void iterator_with_two_packets_where_both_are_taken_before_they_are_read() throws IOException {
        list.add(packet);
        list.add(packet2);

        Packet.Reader iterator = list.read();
        assertEquals(packet,list.take());
        assertEquals(packet2,list.take());
        assertNull(iterator.read());
    }

    @Test
    public void empty_iterator_returns_packet_added_after_creation() throws IOException {
        Packet.Reader iterator = list.read();
        list.add(packet);
        assertEquals(packet,iterator.read());
    }

    @Test
    public void iterator_with_packet_returns_packet_added_after_creation() throws IOException {
        list.add(packet);
        Packet.Reader iterator = list.read();
        list.add(packet2);
        assertEquals(packet,iterator.read());
        assertEquals(packet2,iterator.read());
    }

}
