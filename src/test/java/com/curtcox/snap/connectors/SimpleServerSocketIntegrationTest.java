package com.curtcox.snap.connectors;

import com.curtcox.snap.model.*;
import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.io.IOException;
import java.net.ServerSocket;

import com.curtcox.snap.model.Packet.*;
import static com.curtcox.snap.model.IntegrationTestUtil.*;
import static com.curtcox.snap.model.IntegrationTestUtil.assertResponseFrom;
import static com.curtcox.snap.connectors.UDPTestUtil.flush;
import static com.curtcox.snap.model.TestClock.tick;
import static org.junit.Assert.assertEquals;

public class SimpleServerSocketIntegrationTest {

    @Rule
    public Timeout globalTimeout = Timeout.seconds(2);

    final Network network = Snap.newNetwork(Network.Type.memory);

    Runner runner = Runner.of();

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

    @Test
    public void network_with_TCP_will_respond_to_ping_request() throws IOException {
        ServerSocket socket = new ServerSocket();
        PacketStreamBridge streams = new PacketStreamBridge();
        SimpleServerSocket serverSocket = SimpleServerSocket.forTCP(socket,streams);
        serverSocket.start(runner);
//        network.add(streams);

        Snap receipts = Snap.namedOn("receipts",network);
        PacketReceiptList packets = new PacketReceiptList();
        receipts.on(packets);
        Snap pingSound = addPingSound(network);

        Snap pinger = Snap.namedOn("pinger",network);
        pinger.send(Random.topic(),Ping.REQUEST);
        tick(5);
        //packets = packets.filter(packet -> packet.sender.toString().contains(receipts.host()));

        assertEquals(4,packets);
        assertEquals(4,packets.size());
        assertContainsPingRequest(packets);
        assertContainsPingResponse(packets);
        assertResponseFrom(packets,receipts);
        assertResponseFrom(packets,pingSound);
        assertResponseFrom(packets,pinger);
        flush(streams);
    }

}
