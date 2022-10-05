package com.curtcox.snap.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class CombinedSinkTest {

    @Test
    public void can_create() {
        assertNotNull(new CombinedSink());
    }

    @Test
    public void add_returns_false_when_there_is_no_inner_sink() {
        CombinedSink combined = new CombinedSink();
        assertFalse(combined.add(Random.packet()));
    }

    @Test
    public void add_returns_true_when_an_inner_sink_accepts_the_packet() {
        CombinedSink combined = new CombinedSink();
        combined.add(new PacketReceiptList());
        assertTrue(combined.add(Random.packet()));
    }

    @Test
    public void one_packet_added_to_one_sink() {
        PacketReceiptList sink = new PacketReceiptList();
        CombinedSink combined = new CombinedSink();
        combined.add(sink);

        Packet packet = Random.packet();
        combined.add(packet);

        assertEquals(1,sink.size());
        assertEquals(packet,sink.get(0).packet);
    }

    @Test
    public void two_packets_added_to_two_sinks() {
        PacketReceiptList sink1 = new PacketReceiptList();
        PacketReceiptList sink2 = new PacketReceiptList();
        CombinedSink combined = new CombinedSink();
        combined.add(sink1);
        combined.add(sink2);

        Packet packet1 = Random.packet();
        Packet packet2 = Random.packet();
        combined.add(packet1);
        combined.add(packet2);

        assertEquals(2,sink1.size());
        assertEquals(packet1,sink1.get(0).packet);
        assertEquals(packet2,sink1.get(1).packet);

        assertEquals(2,sink2.size());
        assertEquals(packet1,sink2.get(0).packet);
        assertEquals(packet2,sink2.get(1).packet);
    }

    @Test
    public void prevent_adding_the_same_sink_twice() {
        PacketReceiptList sink = new PacketReceiptList();
        CombinedSink combined = new CombinedSink();
        combined.add(sink);
        try {
            combined.add(sink);
            fail();
        } catch (IllegalArgumentException e) {
            String expected = "This sink has already been added. Adding it again would produce duplicate deliveries.";
            assertEquals(expected,e.getMessage());
        }
    }
}
