package com.curtcox.snap.connectors;

import com.curtcox.snap.model.Packet;
import com.curtcox.snap.model.Random;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;

import static org.junit.Assert.*;

public class UDPTest {

    @Rule
    public Timeout globalTimeout = Timeout.seconds(1);

    @Test
    public void can_create_reader() throws SocketException {
        assertNotNull(new UDP.Reader(new DatagramSocket()));
    }

    @Test
    public void can_create_writer() throws SocketException {
        assertNotNull(new UDP.Writer(null,null));
    }

    @Test
    public void can_write_to_writer() throws IOException {
        Packet packet = Random.packet();
        DatagramSocket socket = new DatagramSocket();
        InetSocketAddress address =  new InetSocketAddress(InetAddress.getByAddress(
                new byte[]{127,0,0,1}),4242);
        Packet.Writer writer = new UDP.Writer(socket,address);
        writer.write(packet);
    }

    @Test
    public void read_is_empty_when_no_packet_to_read() throws IOException {
        Packet.Reader reader = new UDP.Reader(new DatagramSocket());
        Packet packet = reader.read(Packet.ANY);
        assertNull(packet);
    }

    @Test
    public void can_read_packet_written_to_writer() throws IOException {
        Packet packet = Random.packet();
        DatagramSocket socket = new DatagramSocket();
        int port = 4242;
        InetSocketAddress address =  new InetSocketAddress(InetAddress.getByAddress(
                new byte[]{127,0,0,1}),port);
        Packet.Writer writer = new UDP.Writer(socket,address);
        writer.write(packet);
        UDP.Reader reader = new UDP.Reader(new DatagramSocket(port));
        reader.receiveDatagram();
        Packet received = reader.read(Packet.ANY);
        assertEquals(packet,received);
    }

    @Test
    public void can_create_io() throws IOException {
        assertNotNull(UDP.io());
    }

    @Test
    public void can_read_packet_written_to_io() throws IOException {
        Packet packet = Random.packet();
        Packet.Writer writer = UDP.io();
        writer.write(packet);
        UDP.Reader reader = new UDP.Reader(new DatagramSocket(UDP.PORT));
        reader.receiveDatagram();
        Packet received = reader.read(Packet.ANY);
        assertEquals(packet,received);
    }

}
