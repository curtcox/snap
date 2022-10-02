package com.curtcox.snap.connectors;

import com.curtcox.snap.model.PacketReaderWriter;
import com.curtcox.snap.model.Random;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.io.*;

import com.curtcox.snap.model.Packet;
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
        InputStream in = new ByteArrayInputStream(new byte[0]);
        OutputStream out = new ByteArrayOutputStream();
        StreamIO streamIO = new StreamIO(in,out);
        PacketReaderWriter readerWriter = PacketReaderWriter.from(streamIO);
        bridge.accept(streamIO);
        readerWriter.write(packet);

        Packet read = bridge.read(ANY);
        assertEquals(packet,read);
    }

    @Test
    public void read_returns_packet_from_2_streams() {
        fail();
    }

    @Test
    public void write_discards_packet_when_no_streams() {
        fail();
    }

    @Test
    public void write_sends_packet_to_1_stream() {
        fail();
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
