package com.curtcox.snap.model;

import org.junit.Test;

import java.io.IOException;
import static com.curtcox.snap.model.Packet.ANY;

import static org.junit.Assert.*;

public class PacketListTest {

    PacketReaderFactory list = new PacketReaderFactory();

    Packet packet = Packet.builder()
            .sender(new Packet.Sender("A"))
            .topic(new Packet.Topic("one"))
            .message("1")
            .build();
    Packet packet2 = Packet.builder()
            .sender(new Packet.Sender("B"))
            .topic(new Packet.Topic("two"))
            .message("2")
            .build();
    Packet packet3 = Packet.builder()
            .sender(new Packet.Sender("C"))
            .topic(new Packet.Topic("three"))
            .message("3")
            .build();

    @Test
    public void can_create() {
        assertNotNull(new PacketReaderFactory());
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
    public void take_is_null_after_only_packet_is_taken_via_reading() throws IOException {
        list.add(packet);
        Packet.Reader iterreaderator = list.read(ANY);
        assertEquals(packet, iterreaderator.read(ANY));
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
    public void take_is_null_after_both_packets_are_taken_via_reading() throws IOException {
        list.add(packet);
        list.add(packet2);
        Packet.Reader reader = list.read(ANY);
        assertEquals(packet,reader.read(ANY));
        assertEquals(packet2,reader.read(ANY));
        assertNull(list.take());
    }

    @Test
    public void take_is_null_after_3_packets_are_taken_via_reading() throws IOException {
        list.add(packet);
        list.add(packet2);
        list.add(packet3);
        Packet.Reader reader = list.read(ANY);
        assertEquals(packet,reader.read(ANY));
        assertEquals(packet2,reader.read(ANY));
        assertEquals(packet3,reader.read(ANY));
        assertNull(list.take());
    }

    @Test
    public void reading_with_no_packets_returns_null() throws IOException {
        assertNull(list.read(ANY).read(ANY));
    }

    @Test
    public void iterator_with_no_packets_next_throws_exception() throws IOException {
        assertNull(list.read(ANY).read(ANY));
    }

    @Test
    public void reading_one_packet() throws IOException {
        list.add(packet);

        assertEquals(packet,list.read(ANY).read(ANY));
    }

    @Test
    public void taking_one_packet_removes_it_from_the_list() throws IOException {
        list.add(packet);

        assertEquals(packet,list.take());
        assertNull(list.read(ANY).read(ANY));
    }

    @Test
    public void taking_two_packets_removes_them_from_the_list() throws IOException {
        list.add(packet);
        list.add(packet2);

        Packet.Reader reader = list.read(ANY);

        assertEquals(packet,list.take());
        assertEquals(packet2,list.take());
        assertNull(reader.read(ANY));
        assertNull(list.read(ANY).read(ANY));
    }

    @Test
    public void reading_two_packets() throws IOException {
        list.add(packet);
        list.add(packet2);

        Packet.Reader reader = list.read(ANY);

        assertEquals(packet,reader.read(ANY));
        assertEquals(packet2,reader.read(ANY));
    }

    @Test
    public void reader_with_one_packet_that_is_taken_before_it_is_read_indicates_no_next() throws IOException {
        list.add(packet);

        Packet.Reader reader = list.read(ANY);

        assertEquals(packet,list.take());
        assertNull(reader.read(ANY));
    }

    @Test
    public void reader_with_one_packet_that_is_taken_before_it_is_read_throws_exception() throws IOException {
        list.add(packet);
        Packet.Reader reader = list.read(ANY);
        assertEquals(packet,list.take());
        assertNull(reader.read(ANY));
    }

    @Test
    public void list_with_two_packets_where_both_are_taken() {
        list.add(packet);
        list.add(packet2);

        assertEquals(packet,list.take());
        assertEquals(packet2,list.take());
    }

    @Test
    public void setList_with_two_packets_where_both_are_taken_before_they_are_read() throws IOException {
        list.add(packet);
        list.add(packet2);

        Packet.Reader reader = list.read(ANY);
        assertEquals(packet,list.take());
        assertEquals(packet2,list.take());
        assertNull(reader.read(ANY));
    }

    @Test
    public void empty_list_returns_packet_added_after_creation() throws IOException {
        Packet.Reader reader = list.read(Packet.ANY);
        list.add(packet);
        assertEquals(packet,reader.read(Packet.ANY));
    }

    @Test
    public void setList_with_packet_returns_packet_added_after_creation() throws IOException {
        list.add(packet);
        Packet.Reader reader = list.read(ANY);
        list.add(packet2);
        assertEquals(packet,reader.read(ANY));
        assertEquals(packet2,reader.read(ANY));
    }

}
