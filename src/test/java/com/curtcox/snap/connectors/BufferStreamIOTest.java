package com.curtcox.snap.connectors;

import com.curtcox.snap.model.Packet;
import com.curtcox.snap.model.PacketReaderWriter;
import com.curtcox.snap.model.Random;
import org.junit.Test;

import java.io.IOException;

import static com.curtcox.snap.model.Packet.*;
import static org.junit.Assert.*;

public class BufferStreamIOTest {

    @Test
    public void with_no_packets_returns_null_when_read() throws IOException {
        Packet.IO io = BufferStreamIO.with();

        assertNull(io.read(ANY));
    }

    @Test
    public void with_1_packets_returns_packet_when_read() throws IOException {
        Packet packet = Random.packet();
        Packet.IO io = BufferStreamIO.with(packet);

        assertEquals(packet,io.read(ANY));
    }

    @Test
    public void with_2_packets_returns_packets_when_read() throws IOException {
        Packet packet1 = Random.packet();
        Packet packet2 = Random.packet();
        Packet.IO io = BufferStreamIO.with(packet1,packet2);

        assertEquals(packet1,io.read(ANY));
        assertEquals(packet2,io.read(ANY));
    }

    @Test
    public void written_is_empty_when_no_packets_written() {
        BufferStreamIO io = BufferStreamIO.with();

        assertTrue(io.written.isEmpty());
    }

    @Test
    public void written_has_packet_when_packet_is_written() throws IOException {
        Packet packet = Random.packet();
        BufferStreamIO io = BufferStreamIO.with();
        io.write(packet);
        assertEquals(1,io.written.size());
        assertEquals(packet,io.written.get(0));
    }

}
