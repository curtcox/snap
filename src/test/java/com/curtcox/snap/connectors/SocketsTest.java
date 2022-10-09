package com.curtcox.snap.connectors;

import org.junit.Test;

import java.net.*;

import static org.junit.Assert.*;

public class SocketsTest {

    @Test
    public void socketAddress_127_0_0_1_8080() throws UnknownHostException {
        InetSocketAddress inetSocketAddress = Sockets.socketAddress(127, 0, 0, 1, 8080);
        InetAddress address = inetSocketAddress.getAddress();
        assertTrue(address.isLoopbackAddress());
        byte[] bytes = address.getAddress();
        assertEquals(127,bytes[0]);
        assertEquals(0,bytes[1]);
        assertEquals(0,bytes[2]);
        assertEquals(1,bytes[3]);
        assertEquals(8080,inetSocketAddress.getPort());
    }

    @Test
    public void address_127_0_0_1() throws UnknownHostException {
        InetAddress address = Sockets.address(127, 0, 0, 1);
        assertTrue(address.isLoopbackAddress());
        byte[] bytes = address.getAddress();
        assertEquals(127,bytes[0]);
        assertEquals(0,bytes[1]);
        assertEquals(0,bytes[2]);
        assertEquals(1,bytes[3]);
    }

    @Test
    public void address_224_192_168_1() throws UnknownHostException {
        InetAddress address = Sockets.address(224, 192, 168, 2);
        assertFalse(address.isLoopbackAddress());
        byte[] bytes = address.getAddress();
        byte b = (byte) 0xe0; // 224 == e0
        assertEquals(b,bytes[0]);
        assertEquals((byte)192,bytes[1]);
        assertEquals((byte)168,bytes[2]);
        assertEquals(2,bytes[3]);
    }

    @Test
    public void address_224_0_0_251_is_multicast() throws UnknownHostException {
        InetAddress address = Sockets.address(224, 0, 0, 251);
        assertTrue(address.isMulticastAddress());
    }

    @Test
    public void address_224_0_0_224_is_multicast() throws UnknownHostException {
        InetAddress address = Sockets.address(224, 0, 0, 224);
        assertTrue(address.isMulticastAddress());
    }

}
