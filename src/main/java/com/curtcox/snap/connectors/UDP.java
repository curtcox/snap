package com.curtcox.snap.connectors;

import com.curtcox.snap.model.*;
import com.curtcox.snap.model.Packet.*;

import java.io.IOException;
import java.net.*;
import java.util.*;

public final class UDP {

    final static int PORT = 0xe0e0;
    final static InetSocketAddress ADDRESS = socketAddress();

    final static Runner runner = Runner.of();

    public static final class Reader implements Packet.Reader {
        private final ConcurrentPacketList packets = new ConcurrentPacketList();
        private final DatagramSocket socket;

        private Reader(DatagramSocket socket) {
            this.socket  = socket;
        }

        public static Reader from(DatagramSocket socket) {
            return from(socket,runner);
        }

        static synchronized Reader from(DatagramSocket socket, Runner runner) {
            Reader reader = new Reader(socket);
            runner.periodically(() -> reader.receiveDatagram());
            return reader;
        }

        private void receiveDatagram() {
            try {
                receiveDatagram0();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void receiveDatagram0() throws IOException {
            byte[] buf    = new byte[Packet.MAX_SIZE];
            DatagramPacket datagram = new DatagramPacket(buf, buf.length);
            socket.receive(datagram);
            process(datagram);
        }

        void process(DatagramPacket datagram) throws IOException {
            packets.add(packet(datagram));
        }

        private Packet packet(DatagramPacket packet) throws IOException {
            return Packet.from(Bytes.bytes(packet.getData()));
        }

        @Override
        public Packet read(Filter filter) throws IOException {
            return packets.read(filter);
        }
    }

    public static final class Writer implements Packet.Writer {
        private final DatagramSocket socket;
        private final InetSocketAddress address;

        Writer(DatagramSocket socket, InetSocketAddress address) {
            this.socket  = socket;
            this.address = address;
        }

        @Override
        public void write(Packet packet) throws IOException {
            socket.send(datagramToSend(packet));
        }

        private DatagramPacket datagramToSend(Packet packet) {
            byte[] bytes = packet.asBytes().value();
            return new DatagramPacket(bytes, bytes.length,address);
        }

    }

    public static void main(String[] args) throws IOException {
        on(network());
    }

    static void on(Network network) throws IOException {
        network.add(io());
    }

    static Network network() {
        return SimpleNetwork.newPolling();
    }

    public static IO io() throws IOException {
        return io(runner);
    }

    static synchronized IO io(Runner runner) throws IOException {
        return new PacketReaderWriter(
                Reader.from(newDatagramSocket(ADDRESS),runner),
                new Writer(newDatagramSocket(),ADDRESS));
    }

    private static Map<InetSocketAddress,DatagramSocket> sockets = Collections.synchronizedMap(new HashMap<>());
    static DatagramSocket newDatagramSocket(InetSocketAddress address) throws IOException {
        try {
            if (sockets.containsKey(address)) {
                return sockets.get(address);
            }
            DatagramSocket socket = rightSocketType(address);
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

     static DatagramSocket newDatagramSocket() throws IOException {
        try {
            return new DatagramSocket();
        } catch (SocketException e) {
            throw new IOException("Unable to create socket.",e);
        }
    }

    /**
     * This picks a suitable address type that doesn't seem reserved and should be easy to grab (<1024).
     * Something better and harder can be done when justified.
     * Multicast DNS uses IPv4 address 224.0.0.251 or IPv6 address ff02::fb UDP port 5353
     * https://en.wikipedia.org/wiki/Multicast_address
     * https://en.wikipedia.org/wiki/Multicast_DNS
     *
     * So, we pick e0.0.0.e0:e0e0 == 224.0.0.224:57568
     */
    private static InetSocketAddress socketAddress0() throws UnknownHostException {
        byte b = (byte) 0xe0; // 224 == e0
        return new InetSocketAddress(InetAddress.getByAddress(new byte[]{b,0,0,b}),PORT);
    }

    private static InetSocketAddress socketAddress() {
        try {
            return socketAddress0();
        } catch (UnknownHostException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

}
