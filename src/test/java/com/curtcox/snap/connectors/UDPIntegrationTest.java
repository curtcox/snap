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

    private void addPingSound() {
        com.curtcox.snap.ui.Ping.on(Snap.on(network));
    }
    private void assertContainsPingResponse(List<Receipt> receipts) {
        if (contains(receipts, receipt -> Ping.isResponse.test(receipt.packet))) return;
        fail("No ping responses in " + receipts);
    }

    private void assertContainsPingRequest(List<Receipt> receipts) {
        if (contains(receipts, receipt -> Ping.isRequest.test(receipt.packet))) return;
        fail("No ping responses in " + receipts);
    }

    private boolean contains(List<Receipt> receipts, Predicate<Receipt> predicate) {
        for (Receipt receipt : receipts) {
            if (predicate.test(receipt)) {
                return true;
            }
        }
        return false;
    }

    @Test
    public void network_with_UDP_will_respond_to_ping_request() throws IOException {
        PacketReceiptList packets = new PacketReceiptList();
        IO io = UDP.io(runner);
        network.add(io);
        Snap.on(network).on(packets);
        addPingSound();

        Snap.on(network).send(Random.topic(),Ping.REQUEST);
        tick(5);

        System.out.println(packets);
        assertEquals(3,packets.size());
        assertContainsPingRequest(packets);
        assertContainsPingResponse(packets);
        flush(io);
    }

}
