package com.curtcox.snap.connectors;

import com.curtcox.snap.model.*;
import com.curtcox.snap.model.Packet.*;
import com.curtcox.snap.model.Random;
import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.io.IOException;

import static com.curtcox.snap.model.IntegrationTestUtil.*;
import static com.curtcox.snap.connectors.UDPTestUtil.flush;
import static com.curtcox.snap.model.TestClock.tick;
import static org.junit.Assert.*;

public class UDPIntegrationTest {

    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    final Network network = Snap.newNetwork(Network.Type.memory);

    Runner runner = Runner.of();

    @After
    public void stop() {
        runner.stop();
    }

    @Test
    public void network_with_no_UDP_will_respond_to_ping_request() {
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
    public void network_with_UDP_will_respond_to_ping_request() throws IOException {
        IO io = UDP.io(runner);
        flush(io);
        network.add(io);
        Snap recorder = Snap.namedOn("recorder",network);
        PacketReceiptList receipts = PacketReceiptList.on(recorder);
        Snap pingSound = addPingSound(network);

        Snap pinger = Snap.namedOn("pinger",network);
        pinger.send(Random.topic(),Ping.REQUEST);
        tick(5);
        receipts = receipts.filter(packet -> packet.sender.toString().contains(recorder.host()));

        assertEquals(2,receipts.size());
        assertContainsPingRequest(receipts);
        assertContainsPingResponse(receipts);
        assertResponseFrom(receipts,pingSound);
        flush(io);
    }

}
