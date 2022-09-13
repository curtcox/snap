package com.curtcox.snap.connectors;

import com.curtcox.snap.model.Bytes;
import com.curtcox.snap.model.Packet;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Queue;

public final class UdpIO implements Packet.IO {

    final Queue<Packet> packets;
    final DatagramSocket socket;
    final InetAddress group;
    final int            port;

    UdpIO(Queue<Packet> packets, DatagramSocket socket,InetAddress group,int port) {
        this.packets = packets;
        this.socket  = socket;
        this.group   = group;
        this.port    = port;
    }

    @Override
    public void write(Packet packet) throws IOException {
        socket.send(datagram(packet));
    }

    private DatagramPacket datagram(Packet packet) {
        byte[] bytes = packet.asBytes().value();
        return new DatagramPacket(bytes, bytes.length,group,port);
    }

    void receive() throws IOException {
        byte[] buf    = new byte[Packet.MAX_SIZE];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        socket.receive(packet);
        process(packet);
    }

    void process(DatagramPacket packet) throws IOException {
        packets.add(packet(packet));
    }

    private Packet packet(DatagramPacket packet) throws IOException {
        return Packet.from(Bytes.bytes(packet.getData()));
    }

    @Override
    public Packet read(Packet.Filter filter) throws IOException {
        return packets.poll();
    }

}
