package com.curtcox.snap.connectors;

import com.curtcox.snap.model.Packet;
import com.curtcox.snap.model.PacketReceiptList;
import com.curtcox.snap.model.Ping;
import com.curtcox.snap.model.Snap;

import java.util.List;
import java.util.function.Predicate;

import static org.junit.Assert.fail;

final class IntegrationTestUtil {

    static void assertContainsPingResponse(List<Packet.Receipt> receipts) {
        assertContains(receipts, receipt -> Ping.isResponse.test(receipt.packet),"ping response");
    }

    static void assertContainsPingRequest(List<Packet.Receipt> receipts) {
        assertContains(receipts, receipt -> Ping.isRequest.test(receipt.packet),"ping requests");
    }

    static void assertContains(List<Packet.Receipt> receipts, Predicate<Packet.Receipt> predicate, String target) {
        if (contains(receipts, predicate)) return;
        fail("No " + target + " in " + receipts);
    }

    static boolean contains(List<Packet.Receipt> receipts, Predicate<Packet.Receipt> predicate) {
        for (Packet.Receipt receipt : receipts) {
            if (predicate.test(receipt)) {
                return true;
            }
        }
        return false;
    }

    static void assertResponseFrom(PacketReceiptList packets, Snap snap) {
        assertContains(packets, receipt -> {
            Packet packet = receipt.packet;
            return Ping.isResponse.test(packet) && packet.sender.toString().equals(snap.whoami());
        },"ping response from " + snap.whoami());
    }

    static Snap addPingSound(Packet.Network network) {
        Snap snap = Snap.on(network);
        com.curtcox.snap.ui.Ping.on(snap);
        return snap;
    }

}
