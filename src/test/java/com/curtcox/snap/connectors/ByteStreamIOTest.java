package com.curtcox.snap.connectors;

import com.curtcox.snap.model.*;
import com.curtcox.snap.model.Random;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static com.curtcox.snap.model.Packet.*;
import static org.junit.Assert.*;

public class ByteStreamIOTest {

    @Test
    public void with_no_packets_has_0_bytes_available_to_read() throws IOException {
        StreamIO io = ByteStreamIO.with().asStreamIO();

        assertEquals(0,io.in.available());
    }

    @Test
    public void with_no_packets_read() throws IOException {
        StreamIO io = ByteStreamIO.with().asStreamIO();

        assertEquals(-1,io.in.read());
    }

    @Test
    public void with_a_packets_has_some_bytes_available_to_read() throws IOException {
        StreamIO io = ByteStreamIO.with(Random.packet()).asStreamIO();

        assertPositive(io.in.available());
    }

    private void assertPositive(int value) {
        assertTrue(value + " should be > 0",value>0);
    }

    @Test
    public void with_1_packets_returns_packet_when_read() throws IOException {
        Packet packet = Random.packet();
        StreamIO io = ByteStreamIO.with(packet).asStreamIO();
        List<Packet> packets = InputStreamPacketReader.readWaiting(io.in);

        assertEquals(1,packets.size());
        assertEquals(packet,packets.get(0));
    }

    @Test
    public void with_2_packets_returns_packets_when_read() throws IOException {
        Packet packet1 = Random.packet();
        Packet packet2 = Random.packet();
        StreamIO io = ByteStreamIO.with(packet1,packet2).asStreamIO();
        List<Packet> packets = InputStreamPacketReader.readWaiting(io.in);

        assertEquals(2,packets.size());
        assertEquals(packet1,packets.get(0));
        assertEquals(packet2,packets.get(1));
    }

    @Test
    public void written_is_empty_when_no_packets_written() throws IOException {
        ByteStreamIO io = ByteStreamIO.with();

        assertEquals(0,io.getWrittenTo().size());
    }

    @Test
    public void written_has_packet_when_packet_is_written() throws IOException {
        Packet packet = Random.packet();
        ByteStreamIO io = ByteStreamIO.with();
        OutputStreamPacketWriter.writeTo(io.asStreamIO().out,packet);

        List<Packet> written = io.getWrittenTo();
        assertEquals(1,written.size());
        assertEquals(packet,written.get(0));
    }

    @Test
    public void written_has_2_packets_when_2_packets_are_written() throws IOException {
        Packet packet1 = Random.packet();
        Packet packet2 = Random.packet();
        ByteStreamIO io = ByteStreamIO.with();
        OutputStreamPacketWriter.writeTo(io.asStreamIO().out,packet1,packet2);

        List<Packet> written = io.getWrittenTo();
        assertEquals(2,written.size());
        assertEquals(packet1,written.get(0));
        assertEquals(packet2,written.get(1));
    }

    @Test
    public void asStreamIO_returns_stream() {
        ByteStreamIO io = ByteStreamIO.with();

        StreamIO streamIO = io.asStreamIO();
        assertNotNull(streamIO);
        assertNotNull(streamIO.in);
        assertNotNull(streamIO.out);
    }

    @Test
    public void asStreamIO_in_has_0_available_when_none_written() throws IOException {
        ByteStreamIO io = ByteStreamIO.with();

        InputStream in = io.asStreamIO().in;
        assertEquals(0,in.available());
    }

    @Test
    public void can_read_packet_from_asStreamIO() throws IOException {
        Packet packet = Random.packet();

        ByteStreamIO io = ByteStreamIO.with(packet);
        Packet read = new InputStreamPacketReader(io.asStreamIO().in).read(ANY);

        assertEquals(packet,read);
    }

    @Test
    public void can_read_packet_written_to_asStreamIO() throws IOException {
        Packet packet = Random.packet();
        ByteStreamIO io = ByteStreamIO.with(packet);

        new OutputStreamPacketWriter(io.asStreamIO().out).write(packet);

        List<Packet> read = io.getWrittenTo();
        assertEquals(1,read.size());
        assertEquals(packet,read.get(0));
    }

}
