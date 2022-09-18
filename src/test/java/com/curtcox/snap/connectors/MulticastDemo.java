package com.curtcox.snap.connectors;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

// See https://docs.oracle.com/javase/7/docs/api/java/net/MulticastSocket.html
public class MulticastDemo {
    public static void main(String[] args) throws IOException {
        // join a Multicast group and send the group salutations
        String msg = "Hello";
        InetAddress group = InetAddress.getByName("228.5.6.7");
        MulticastSocket socket = new MulticastSocket(6789);
        socket.joinGroup(group);
        DatagramPacket hi = new DatagramPacket(msg.getBytes(), msg.length(), group, 6789);
        socket.send(hi);
        // get their responses!
        byte[] buf = new byte[10];
        DatagramPacket recv = new DatagramPacket(buf, buf.length);
        socket.receive(recv);
        System.out.println("Received " + new String(recv.getData()));
        // OK, I'm done talking - leave the group...
        socket.leaveGroup(group);
    }
}
