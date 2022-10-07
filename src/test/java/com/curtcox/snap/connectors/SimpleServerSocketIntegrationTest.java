package com.curtcox.snap.connectors;

import com.curtcox.snap.model.*;
import com.curtcox.snap.model.Random;
import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.io.*;
import java.net.ServerSocket;
import java.util.*;

import com.curtcox.snap.model.Packet.*;
import static com.curtcox.snap.model.IntegrationTestUtil.*;
import static com.curtcox.snap.model.IntegrationTestUtil.assertResponseFrom;
import static com.curtcox.snap.model.TestClock.tick;
import static org.junit.Assert.assertEquals;

public class SimpleServerSocketIntegrationTest {

    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    Runner runner = Runner.of();

    final Network network = SimpleNetwork.newPolling(runner);


    @After
    public void stop() {
        runner.stop();
    }

    @Test
    public void network_with_no_TCP_will_respond_to_ping_request() {
        PacketReceiptList packets = new PacketReceiptList();
        Snap.on(network).on(packets);
        addPingSound(network);

        Snap.on(network).send(Random.topic(),Ping.REQUEST);
        tick(5);

        assertEquals(2,packets.size());
        assertContainsPingRequest(packets);
        assertContainsPingResponse(packets);
    }

    private PacketStreamBridge tcpSocketBridge(ServerSocket socket) {
        PacketStreamBridge streams = new PacketStreamBridge();
        SimpleServerSocket serverSocket = SimpleServerSocket.forTCP(socket,streams);
        serverSocket.start(runner);
        return streams;
    }

    @Test
    public void network_with_TCP_will_respond_to_ping_request() throws IOException {
        network.add(tcpSocketBridge(new ServerSocket()));

        Snap recorder = Snap.namedOn("recorder",network);
        PacketReceiptList receipts = PacketReceiptList.on(recorder);
        Snap pingSound = addPingSound(network);

        Snap pinger = Snap.namedOn("pinger",network);
        pinger.send(Random.topic(),Ping.REQUEST);
        tick(5);

        assertEquals(2,receipts.size());
        assertContainsPingRequest(receipts);
        assertContainsPingResponse(receipts);
        assertResponseFrom(receipts,pingSound);
    }

    @Test
    public void can_ping_from_TCP_to_network() throws IOException {
        Packet ping = ping();
        ByteStreamIO io = ByteStreamIO.with(ping);
        network.add(tcpSocketBridge(new FakeServerSocket(io.asStreamIO())));

        Snap recorder = Snap.namedOn("recorder",network);
        PacketReceiptList receipts = PacketReceiptList.on(recorder);
        Snap pingSound = addPingSound(network);
        tick(55);

        assertEquals(2,receipts.size());
        assertContainsPingRequest(receipts);
        assertContainsPingResponse(receipts);
        assertResponseFrom(receipts,pingSound);

        List<Packet> packets = io.getWrittenTo();
        assertEquals(2,packets.size());
        Packet pong1 = packets.get(0);
        assertEquals(Ping.RESPONSE,pong1.message);
        assertEquals(Trigger.from(ping),pong1.trigger);
        assertEquals(ping.topic,pong1.topic);
        Packet pong2 = packets.get(0);
        assertEquals(Ping.RESPONSE,pong2.message);
        assertEquals(Trigger.from(ping),pong2.trigger);
        assertEquals(ping.topic,pong2.topic);
    }

    @Test
    public void can_ping_from_TCP_to_network_using_snap() throws IOException {
        Network outside = SimpleNetwork.newPolling(runner);
        Pipe pipe = new Pipe();
        outside.add(PacketReaderWriter.from(pipe.left));
        network.add(tcpSocketBridge(new FakeServerSocket(pipe.right)));

        Snap recorder = Snap.namedOn("recorder",network);
        PacketReceiptList receipts = PacketReceiptList.on(recorder);
        Snap pingSound = addPingSound(network);
        Snap pinger = Snap.namedOn("pinger",outside);
        pinger.send(Random.topic(),Ping.REQUEST);
        tick(55);

        assertEquals(2,receipts.size());
        assertContainsPingRequest(receipts);
        assertContainsPingResponse(receipts);
        assertResponseFrom(receipts,pingSound);
    }

    private Packet ping() {
        return Packet.builder()
                .sender(new Sender("test"))
                .topic(Random.topic())
                .message(Ping.REQUEST)
                .build();
    }

}
