package com.curtcox.snap.model;

import org.junit.*;

import java.io.*;

import static org.junit.Assert.*;

public class SplitReaderFactoryTest {

    Packet packet = Random.packet();
    ConcurrentPacketList packets = new ConcurrentPacketList();
    SplitReaderFactory reader = new SplitReaderFactory(packets);

    @Test
    public void can_create() {
        assertNotNull(new SplitReaderFactory(packets));
    }

    @Test
    public void a_single_reader_can_read_a_packet_from_the_underlying_reader() throws IOException {
        packets.add(packet);
        Packet read = reader.reader(Packet.ANY).read(Packet.ANY);
        assertEquals(packet,read);
    }

    @Test
    public void a_single_reader_can_only_read_a_packet_from_the_underlying_reader_once() throws IOException {
        packets.add(packet);
        Packet.Reader outer = reader.reader(Packet.ANY);
        outer.read(Packet.ANY);
        Packet read2 = outer.read(Packet.ANY);
        assertNull(read2);
    }

    @Test
    public void multiple_readers_can_read_a_packet_from_the_underlying_reader() throws IOException {
        packets.add(packet);
        Packet read1 = reader.reader(Packet.ANY).read(Packet.ANY);
        Packet read2 = reader.reader(Packet.ANY).read(Packet.ANY);
        assertEquals(packet,read1);
        assertEquals(packet,read2);
    }

    @Test
    public void a_single_reader_can_read_2_packets_from_the_underlying_reader() throws IOException {
        Packet packet2 = Random.packet();
        packets.add(packet);
        packets.add(packet2);
        Packet.Reader outer = reader.reader(Packet.ANY);
        assertEquals(packet,outer.read(Packet.ANY));
        assertEquals(packet2,outer.read(Packet.ANY));
        assertNull(outer.read(Packet.ANY));
    }

    @Test
    public void multiple_readers_can_read_2_packets_from_the_underlying_reader() throws IOException {
        Packet packet2 = Random.packet();
        packets.add(packet);
        packets.add(packet2);
        Packet.Reader outer1 = reader.reader(Packet.ANY);
        assertEquals(packet,outer1.read(Packet.ANY));
        assertEquals(packet2,outer1.read(Packet.ANY));
        assertNull(outer1.read(Packet.ANY));
        Packet.Reader outer2 = reader.reader(Packet.ANY);
        assertEquals(packet,outer2.read(Packet.ANY));
        assertEquals(packet2,outer2.read(Packet.ANY));
        assertNull(outer2.read(Packet.ANY));
    }

}
