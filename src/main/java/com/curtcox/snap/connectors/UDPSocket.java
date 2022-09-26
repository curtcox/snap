package com.curtcox.snap.connectors;

import com.curtcox.snap.model.Packet;

import java.io.IOException;
import java.net.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

final class UDPSocket {

    private final DatagramSocket inner;

    private static Map<InetSocketAddress, UDPSocket> sockets = Collections.synchronizedMap(new HashMap<>());

    UDPSocket(DatagramSocket inner) {
        this.inner = inner;
    }

    static UDPSocket newDatagramSocket(InetSocketAddress address) throws IOException {
        try {
            if (sockets.containsKey(address)) {
                return sockets.get(address);
            }
            UDPSocket socket = new UDPSocket(rightSocketType(address));
            sockets.put(address,socket);
            return socket;
        } catch (SocketException e) {
            throw new IOException("Unable to create socket on address " + address,e);
        }
    }

    private static DatagramSocket rightSocketType(InetSocketAddress address) throws IOException {
        if (address.getAddress().isMulticastAddress()) {
            MulticastSocket socket = new MulticastSocket(address.getPort());
            socket.joinGroup(address.getAddress());
            return socket;
        }
        return new DatagramSocket(address);
    }

    static UDPSocket newDatagramSocket() throws IOException {
        try {
            return new UDPSocket(new DatagramSocket());
        } catch (SocketException e) {
            throw new IOException("Unable to create socket.",e);
        }
    }

    DatagramPacket receiveDatagram() throws IOException {
        byte[] buffer = new byte[Packet.MAX_SIZE];
        DatagramPacket datagram = new DatagramPacket(buffer, buffer.length);
        inner.receive(datagram);
        return datagram;
    }

    void send(DatagramPacket datagram) throws IOException {
        inner.send(datagram);
    }

    static InetSocketAddress address(int a, int b, int c, int d, int port)
            throws UnknownHostException {
        return new InetSocketAddress(InetAddress.getByAddress(
                new byte[]{(byte)a,(byte)b,(byte)c,(byte)d}),port);
    }

}
