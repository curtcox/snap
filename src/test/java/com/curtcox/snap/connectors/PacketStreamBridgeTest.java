package com.curtcox.snap.connectors;

import com.curtcox.snap.model.Await;
import com.curtcox.snap.model.PacketReaderWriter;
import com.curtcox.snap.model.Random;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.io.*;
import java.util.List;

import com.curtcox.snap.model.Packet;

import static com.curtcox.snap.connectors.IntegrationTestUtil.assertContains;
import static com.curtcox.snap.model.Packet.*;
import static org.junit.Assert.*;

public class PacketStreamBridgeTest {

    @Rule
    public Timeout globalTimeout = Timeout.seconds(2);

    PacketStreamBridge bridge = new PacketStreamBridge();
    @Test
    public void can_create() {
        assertNotNull(new PacketStreamBridge());
    }

    @Test
    public void read_returns_null_when_there_are_no_streams() throws IOException {
        assertNull(bridge.read(ANY));
    }

    @Test
    public void read_returns_packet_from_1_stream() throws IOException {
        Packet packet = Random.packet();
        bridge.accept(BufferStreamIO.with(packet).asStreamIO());

        Packet read = bridge.read(ANY);
        assertEquals(packet,read);
    }


    @Test
    public void read_returns_packets_from_2_streams() throws IOException {
        Packet packet1 = Random.packet();
        Packet packet2 = Random.packet();
        bridge.accept(BufferStreamIO.with(packet1).asStreamIO());
        bridge.accept(BufferStreamIO.with(packet2).asStreamIO());

        List<Packet> read = Await.packets(bridge);
        assertContains(read,packet1);
        assertContains(read,packet2);
    }

    @Test
    public void write_discards_packet_when_no_streams() throws IOException {
        bridge.write(Random.packet());
    }

    @Test
    public void write_sends_packet_to_1_stream() throws IOException {
        BufferStreamIO stream = BufferStreamIO.with();
        bridge.accept(stream.asStreamIO());
        Packet packet = Random.packet();
        bridge.write(packet);
    }

    @Test
    public void write_sends_packet_to_2_streams() {
        fail();
    }

    @Test
    public void closed_streams_are_discarded() {
        fail();
    }
}
