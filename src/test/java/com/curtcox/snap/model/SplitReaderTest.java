package com.curtcox.snap.model;

import org.junit.*;

import java.io.*;

import static org.junit.Assert.*;

public class SplitReaderTest {

    Packet packet = Random.packet();
    ConcurrentPacketList packets = new ConcurrentPacketList();
    SplitReader reader = new SplitReader(packets);

    @Test
    public void can_create() {
        assertNotNull(new SplitReader(packets));
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
    public void a_single_reader_can_read_2_packets_from_the_underlying_reader() {
        fail();
    }

    @Test
    public void multiple_readers_can_read_2_packets_from_the_underlying_reader() {
        fail();
    }

}
