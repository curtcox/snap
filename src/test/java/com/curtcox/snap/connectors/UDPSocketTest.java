package com.curtcox.snap.connectors;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.io.IOException;
import java.net.DatagramPacket;

import static com.curtcox.snap.connectors.UDPSocket.address;
import static com.curtcox.snap.connectors.UDPSocket.newDatagramSocket;
import static org.junit.Assert.*;

public class UDPSocketTest {

    @Rule
    public Timeout globalTimeout = Timeout.seconds(20);

    @Test
    public void can_create_socket() throws IOException {
        assertNotNull(newDatagramSocket());
    }

    @Test
    public void can_create_socket_on_224_0_0_222_2222() throws IOException {
        assertNotNull(newDatagramSocket(address(224,0,0,222,2222)));
    }

    @Test
    public void can_create_socket_on_224_0_0_224_57568() throws IOException {
        assertNotNull(newDatagramSocket(address(224,0,0,224,57568)));
    }

    @Test
    public void can_create_socket_on_224_0_0_251_5353() throws IOException {
        assertNotNull(newDatagramSocket(address(224,0,0,251,5353)));
    }

    @Test
    public void can_read_socket() throws IOException {
        UDPSocket socket = newDatagramSocket(address(224,0,0,251,5353));
        DatagramPacket packet = socket.receiveDatagram();
        assertNotNull(packet);
    }

    @Test
    public void can_poll_socket() throws IOException {
        UDPSocket socket = newDatagramSocket(address(224,0,0,251,5353));
        for (int i=0; i<100; i++) {
            DatagramPacket packet = socket.receiveDatagram();
            assertNotNull(packet);
            System.out.println(i + "" + new String(packet.getData(),0,packet.getLength()));
        }
    }

}
