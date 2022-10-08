package com.curtcox.snap.connectors;

import com.curtcox.snap.model.*;
import org.junit.*;
import org.junit.rules.Timeout;

import java.io.*;
import java.net.ServerSocket;
import java.util.List;

import static com.curtcox.snap.model.IntegrationTestUtil.assertContains;
import static com.curtcox.snap.model.Packet.*;
import static com.curtcox.snap.model.TestClock.tick;
import static org.junit.Assert.*;

public class PacketStreamBridgeTest {

    @Rule
    public Timeout globalTimeout = Timeout.seconds(2);

    Runner runner = Runner.of();

    @After
    public void stop() {
        runner.stop();
    }

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
        bridge.accept(ByteStreamIO.with(packet).asStreamIO());

        Packet read = bridge.read(ANY);
        assertEquals(packet,read);
    }

    @Test
    public void read_returns_packets_from_2_streams() throws IOException {
        Packet packet1 = Random.packet();
        Packet packet2 = Random.packet();
        bridge.accept(ByteStreamIO.with(packet1).asStreamIO());
        bridge.accept(ByteStreamIO.with(packet2).asStreamIO());

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
        ByteStreamIO stream = ByteStreamIO.with();
        bridge.accept(stream.asStreamIO());
        Packet packet = Random.packet();
        bridge.write(packet);

        List<Packet> written = stream.getWrittenTo();
        assertEquals(1,written.size());
        assertEquals(packet,written.get(0));
    }

    @Test
    public void write_sends_packet_to_2_streams() throws IOException {
        ByteStreamIO stream1 = ByteStreamIO.with();
        bridge.accept(stream1.asStreamIO());
        ByteStreamIO stream2 = ByteStreamIO.with();
        bridge.accept(stream2.asStreamIO());

        Packet packet = Random.packet();
        bridge.write(packet);

        List<Packet> written1 = stream1.getWrittenTo();
        assertEquals(1,written1.size());
        assertEquals(packet,written1.get(0));

        List<Packet> written2 = stream2.getWrittenTo();
        assertEquals(1,written2.size());
        assertEquals(packet,written2.get(0));
    }

    @Test @Ignore // See TODO FIXME
    public void closed_streams_are_not_written_to() throws IOException {
        ByteStreamIO buffer = ByteStreamIO.with();
        StreamIO streamIO = buffer.asStreamIO();
        bridge.accept(streamIO);
        streamIO.out.close();

        bridge.write(Random.packet());

        List<Packet> written = buffer.getWrittenTo();
        assertEquals(0,written.size());
    }

    // Closing has no effect on byte array input/output stream.
    // Custom byte array streams for the tests is too much work.
    // Another cleanup strategy is to only keep the last N streams.
    // Punt for now.

    @Test @Ignore // See TODO FIXME
    public void closed_streams_are_not_read_from() throws IOException {
        ByteStreamIO buffer = ByteStreamIO.with(Random.packet());
        StreamIO streamIO = buffer.asStreamIO();
        bridge.accept(streamIO);
        streamIO.in.close();

        assertNull(bridge.read(ANY));
    }

    @Test @Ignore // See TODO FIXME
    public void streams_are_not_written_to_after_their_input_is_closed() throws IOException {
        ByteStreamIO buffer = ByteStreamIO.with();
        StreamIO streamIO = buffer.asStreamIO();
        bridge.accept(streamIO);
        streamIO.in.close();
        assertNull(bridge.read(ANY));

        bridge.write(Random.packet());

        List<Packet> written = buffer.getWrittenTo();
        assertEquals(0,written.size());
    }

    @Test
    public void fromServerSocket() throws IOException {
        Packet packet = Random.packet();
        ServerSocket serverSocket = new FakeServerSocket(ByteStreamIO.with(packet).asStreamIO());
        PacketStreamBridge bridge = PacketStreamBridge.fromServerSocket(serverSocket, runner);

        tick(5);
        Packet read = bridge.read(ANY);
        assertEquals(packet,read);
    }
}
