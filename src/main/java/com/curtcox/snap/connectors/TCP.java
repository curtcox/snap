package com.curtcox.snap.connectors;

import com.curtcox.snap.model.Packet;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public final class TCP {

    final static int PORT = 0xe0e0;
    final static InetSocketAddress address = new InetSocketAddress("0.0.0.0", PORT);

    public static Packet.IO newServer() throws IOException {
        return newServer(boundServerSocket());
    }

    private static java.net.ServerSocket boundServerSocket() throws IOException {
        java.net.ServerSocket serverSocket = new java.net.ServerSocket();
        serverSocket.bind(address);
        return serverSocket;
    }

    static Packet.IO newServer(java.net.ServerSocket serverSocket) {
        PacketStreamBridge bridge = new PacketStreamBridge();
        SimpleServerSocket.forTCP(serverSocket, bridge);
        return bridge;
    }

    interface ClientSocket {
        StreamIO asStreamIO() throws IOException;

        interface Factory {
            ClientSocket client(Socket accept);
        }
    }
}
