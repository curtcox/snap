package com.curtcox.snap.model;

import org.junit.Test;

import java.util.*;

import static com.curtcox.snap.model.TestClock.tick;
import static org.junit.Assert.*;

public class PacketReceiptListTest {

    final PacketReceiptList list = new PacketReceiptList();

    @Test
    public void empty_list() {
        assertTrue(list.isEmpty());
        assertEquals(0,list.size());
        assertFalse(list.iterator().hasNext());
    }

    @Test
    public void list_with_one_element() {
        Packet packet = Random.packet();
        list.add(packet);
        assertFalse(list.isEmpty());
        assertEquals(1,list.size());
        assertTrue(list.iterator().hasNext());

        Iterator<Packet.Receipt> iterator = list.iterator();
        assertEquals(packet,iterator.next().packet);
        assertFalse(iterator.hasNext());
    }

    @Test
    public void toString_contains_packet_string() {
        Packet packet = Random.packet();
        list.add(packet);
        assertContains(list.toString(),packet.toString());
    }

    @Test
    public void filtered_returns_list_of_items_that_passes_filter() {
        Packet.Topic topic1 = Random.topic();
        Packet.Topic topic2 = Random.topic();
        Packet.Builder builder = Packet.builder()
                .sender(Random.sender())
                .message("");
        int count = 10;
        for (int i=0; i<count; i++) {
            list.add(builder.topic(topic1).build());
            list.add(builder.topic(topic2).build());
        }
        PacketReceiptList all1 = list.filter(packet -> packet.topic.equals(topic1));
        PacketReceiptList all2 = list.filter(packet -> packet.topic.equals(topic2));
        assertEquals(count,all1.size());
        assertEquals(count,all2.size());
    }

    @Test
    public void on_snap_includes_packets_received_by_snap() {
        Runner runner = Runner.of();
        SimpleNetwork network = SimpleNetwork.newPolling(runner);
        Snap receiver = Snap.on(network);
        Snap sender = Snap.on(network);
        PacketReceiptList receipts = PacketReceiptList.on(receiver);
        List<Packet> packets = new ArrayList<>();
        int count = 5;
        for (int i=0; i<count; i++) {
            Packet packet = Random.packet();
            packets.add(packet);
            sender.send(packet);
        }
        tick(count);
        assertEquals(count,receipts.size());
        for (int i=0; i<count; i++) {
            assertEquals(packets.get(i),receipts.get(i).packet);
        }
        runner.stop();
    }

    @Test
    public void on_snap_ignores_packets_sent_by_snap() {
        Runner runner = Runner.of();
        SimpleNetwork network = SimpleNetwork.newPolling(runner);
        Snap.on(network);
        Snap sender = Snap.on(network);
        PacketReceiptList receipts = PacketReceiptList.on(sender);
        int count = 5;
        for (int i=0; i<count; i++) {
            sender.send(Random.packet());
        }
        tick(count);
        assertEquals(0,receipts.size());
        runner.stop();
    }

    private void assertContains(String whole, String part) {
        assertTrue(whole + " should contain " + part, whole.contains(part));
    }

}
