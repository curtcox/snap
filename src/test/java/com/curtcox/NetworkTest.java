package com.curtcox;

import org.junit.Test;

import static org.junit.Assert.*;

public class NetworkTest {

    @Test
    public void can_create() {
        assertNotNull(new Network());
    }

    @Test
    public void messsages_can_be_sent_over_a_network() {
        Network network = new Network();
        Snap snap1 = Snap.on(network);
        Snap snap2 = Snap.on(network);

        snap1.send("phone","ring");
        Packet packet = snap2.listen();
        assertEquals("phone",packet.topic);
        assertEquals("ring",packet.message);
    }

}
