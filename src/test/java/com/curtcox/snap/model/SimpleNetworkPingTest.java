package com.curtcox.snap.model;

import org.junit.*;
import org.junit.rules.Timeout;

import static com.curtcox.snap.model.IntegrationTestUtil.*;
import static com.curtcox.snap.model.TestClock.tick;
import static org.junit.Assert.*;

public class SimpleNetworkPingTest {

    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    final Packet.Network network = Snap.newNetwork(Packet.Network.Type.memory);

    Runner runner = Runner.of();

    @After
    public void stop() {
        runner.stop();
    }

    @Test
    public void network_will_respond_to_ping_request() {
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
    public void network_with_ping_sound_will_respond_to_ping_request() {
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

}
