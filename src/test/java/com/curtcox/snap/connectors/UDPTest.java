package com.curtcox.snap.connectors;

import com.curtcox.snap.model.Packet;
import com.curtcox.snap.model.Random;
import com.curtcox.snap.model.Runner;
import org.junit.After;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.io.IOException;
import java.net.*;
import java.util.List;

import static com.curtcox.snap.connectors.UDPTestUtil.flush;
import static com.curtcox.snap.model.TestClock.tick;
import static org.junit.Assert.*;

public class UDPTest {

    static final int annoyinglyHigh = 20;
    // Lower values seem fine when this test is run alone for some reason.

    @Rule
    public Timeout globalTimeout = Timeout.seconds(annoyinglyHigh);

    Runner runner = Runner.of();

    @After
    public void stop() {
        runner.stop();
    }
    @Test
    public void address_is_multicast() {
        assertTrue(UDP.ADDRESS.getAddress().isMulticastAddress());
    }

    @Test
    public void can_create_reader() throws IOException {
        Runner runner = Runner.of();
        assertNotNull(UDP.Reader.from(UDP.newDatagramSocket(),runner));
        runner.stop();
    }

    @Test
    public void can_create_writer() {
        assertNotNull(new UDP.Writer(null,null));
    }

    @Test
    public void can_write_to_writer() throws IOException {
        Packet packet = Random.packet();
        DatagramSocket socket = UDP.newDatagramSocket();
        InetSocketAddress address = address(127,0,0,1,4242);
        Packet.Writer writer = new UDP.Writer(socket,address);
        writer.write(packet);
    }

    @Test
    public void read_is_empty_when_no_packet_to_read() throws IOException {
        Packet.Reader reader = UDP.Reader.from(UDP.newDatagramSocket(),runner);
        Packet packet = reader.read(Packet.ANY);
        assertNull(packet);
    }

    @Test
    public void can_create_io() throws IOException {
        assertNotNull(UDP.io(runner));
    }

    @Test
    public void can_create_reader_for_io() throws IOException {
        assertNotNull(UDP.Reader.from(UDP.newDatagramSocket(UDP.ADDRESS),runner));
    }

    @Test
    public void can_create_writer_for_io() throws IOException {
        assertNotNull(new UDP.Writer(UDP.newDatagramSocket(),UDP.ADDRESS));
    }

    private static InetSocketAddress address(int a, int b, int c, int d, int port)
            throws UnknownHostException {
        return new InetSocketAddress(InetAddress.getByAddress(
                new byte[]{(byte)a,(byte)b,(byte)c,(byte)d}),port);
    }

    @Test
    public void can_read_packet_written_to_writer_when_broadcast_address() throws IOException {
        Packet packet = Random.packet();
        InetSocketAddress address = address(224,0,0,222,2222);
        DatagramSocket socket = UDP.newDatagramSocket(address);
        Packet.Writer writer = new UDP.Writer(socket,address);
        UDP.Reader reader = UDP.Reader.from(socket,runner);
        flush(reader);

        writer.write(packet);
        delay();

        List<Packet> received = flush(reader);
        assertTrue(received + " should contain " + packet,received.contains(packet));
    }
    @Ignore // This test will run OK by itself, but fails if run with everything else.
    @Test
    public void can_read_packet_written_to_io_using_io() throws IOException {
        Packet packet = Random.packet();
        Packet.IO io = UDP.io(runner);
        flush(io,annoyinglyHigh);

        io.write(packet);
        delay();

        List<Packet> received = flush(io,annoyinglyHigh);
        // This may contain responses from the live network, since we are using the default port.
        assertTrue(received + " should contain " + packet,received.contains(packet));
    }

    private static void delay() {
        tick(20);
    }
}
