package com.curtcox.snap.connectors;

import com.curtcox.snap.model.*;

import java.io.IOException;
import java.net.*;

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
        return PacketStreamBridge.fromServerSocket(serverSocket,Runner.of());
    }

    public static Packet.IO newClient(int a1, int a2, int a3, int a4) throws IOException {
        return newClient(Sockets.address(a1,a2,a3,a4),PORT);
    }

    public static Packet.IO newClient(InetAddress address, int port) throws IOException {
        Socket socket = new Socket(address,port);
        return new PacketReaderWriter(
                new InputStreamPacketReader(socket.getInputStream()),
                new OutputStreamPacketWriter(socket.getOutputStream())
        );
    }

    interface ClientSocket {
        StreamIO asStreamIO() throws IOException;

        interface Factory {
            ClientSocket client(Socket accept);
        }
    }
}
