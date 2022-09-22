package com.curtcox.snap.connectors;

import com.curtcox.snap.model.*;
import com.curtcox.snap.model.Packet.*;
import com.curtcox.snap.model.Random;
import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.io.IOException;
import java.util.*;
import java.util.function.Predicate;

import static com.curtcox.snap.connectors.UDPTestUtil.flush;
import static com.curtcox.snap.model.TestClock.tick;
import static org.junit.Assert.*;

public class UDPIntegrationTest {

    @Rule
    public Timeout globalTimeout = Timeout.seconds(2);

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
        addPingSound();

        Snap.on(network).send(Random.topic(),Ping.REQUEST);
        tick(5);

        System.out.println(packets);
        assertEquals(2,packets.size());
        assertContainsPingRequest(packets);
        assertContainsPingResponse(packets);
    }

    private Snap addPingSound() {
        Snap snap = Snap.on(network);
        com.curtcox.snap.ui.Ping.on(snap);
        return snap;
    }
    private void assertContainsPingResponse(List<Receipt> receipts) {
        assertContains(receipts, receipt -> Ping.isResponse.test(receipt.packet),"ping response");
    }

    private void assertContainsPingRequest(List<Receipt> receipts) {
        assertContains(receipts, receipt -> Ping.isRequest.test(receipt.packet),"ping requests");
    }

    private void assertContains(List<Receipt> receipts, Predicate<Receipt> predicate,String target) {
        if (contains(receipts, predicate)) return;
        fail("No " + target + " in " + receipts);
    }

    private boolean contains(List<Receipt> receipts, Predicate<Receipt> predicate) {
        for (Receipt receipt : receipts) {
            if (predicate.test(receipt)) {
                return true;
            }
        }
        return false;
    }

    private void assertResponseFrom(PacketReceiptList packets, Snap snap) {
        assertContains(packets, receipt -> {
            Packet packet = receipt.packet;
            return Ping.isResponse.test(packet) && packet.sender.toString().equals(snap.whoami());
        },"ping response from " + snap.whoami());
    }

    @Test
    public void network_with_UDP_will_respond_to_ping_request() throws IOException {
        PacketReceiptList packets = new PacketReceiptList();
        IO io = UDP.io(runner);
        network.add(io);
        Snap snap1 = Snap.on(network);
        snap1.on(packets);
        Snap snap2 = addPingSound();

        Snap snap3 = Snap.on(network);
        snap3.send(Random.topic(),Ping.REQUEST);
        tick(5);
        packets = packets.filter(packet -> packet.sender.toString().contains(snap1.host()));

        System.out.println(packets);
        assertEquals(4,packets.size());
        assertContainsPingRequest(packets);
        assertContainsPingResponse(packets);
        assertResponseFrom(packets,snap1);
        assertResponseFrom(packets,snap2);
        assertResponseFrom(packets,snap3);
        flush(io);
    }

}
