package com.curtcox.snap.connectors;

import org.junit.Test;

import static org.junit.Assert.*;

public class PacketStreamBridgeTest {

    @Test
    public void can_create() {
        assertNotNull(new PacketStreamBridge());
    }

    @Test
    public void read_returns_null_when_there_are_no_streams() {
        fail();
    }

    @Test
    public void read_returns_packet_from_1_stream() {
        fail();
    }

    @Test
    public void read_returns_packet_from_2_streams() {
        fail();
    }

    @Test
    public void write_discards_packet_when_no_streams() {
        fail();
    }

    @Test
    public void write_sends_packet_to_1_stream() {
        fail();
    }

    @Test
    public void write_sends_packet_to_2_streams() {
        fail();
    }

    @Test
    public void closed_streams_are_discarded() {
        fail();
    }
}
