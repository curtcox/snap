package com.curtcox.snap.connectors;

import com.curtcox.snap.model.Packet;
import com.curtcox.snap.model.Random;
import com.curtcox.snap.model.Runner;
import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.io.IOException;
import java.net.*;

import static com.curtcox.snap.model.TestClock.tick;
import static org.junit.Assert.*;

public class UDPTest {

    @Rule
    public Timeout globalTimeout = Timeout.seconds(3);

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
    public void can_create_writer() throws SocketException {
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
        delay();
        Packet packet = Random.packet();
        InetSocketAddress address = address(224,0,0,222,2222);
        DatagramSocket socket = UDP.newDatagramSocket(address);
        Packet.Writer writer = new UDP.Writer(socket,address);
        UDP.Reader reader = UDP.Reader.from(socket,runner);

        writer.write(packet);
        delay();

        Packet received = reader.read(Packet.ANY);
        assertEquals(packet,received);
    }

    @Test
    public void can_read_packet_written_to_io_using_io() throws IOException {
        delay();
        Packet packet = Random.packet();
        Packet.IO io = UDP.io(runner);
        io.write(packet);
        delay();
        Packet received = io.read(Packet.ANY);
        assertEquals(packet,received);
    }

    private static void delay() {
        tick(20);
    }
}
