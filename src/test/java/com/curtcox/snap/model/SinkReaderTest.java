package com.curtcox.snap.model;

import org.junit.*;

import java.io.*;

import static org.junit.Assert.*;

public class SinkReaderTest {

    @Test
    public void can_create() {
        assertNotNull(new SinkReader());
    }

    @Test
    public void read_returns_null_when_there_are_no_packets() throws IOException {
        SinkReader reader = new SinkReader();
        assertNull(reader.read(Packet.ANY));
    }

    @Test
    public void read_returns_first_packet() throws IOException {
        SinkReader reader = new SinkReader();
        Packet packet = Random.packet();
        reader.add(packet);
        assertEquals(packet,reader.read(Packet.ANY));
        assertNull(reader.read(Packet.ANY));
    }

    @Test
    public void read_returns_first_two_packets() throws IOException {
        SinkReader reader = new SinkReader();
        Packet packet1 = Random.packet();
        Packet packet2 = Random.packet();
        reader.add(packet1);
        reader.add(packet2);
        assertEquals(packet1,reader.read(Packet.ANY));
        assertEquals(packet2,reader.read(Packet.ANY));
        assertNull(reader.read(Packet.ANY));
    }

}
