package com.curtcox.snap.model;

import org.junit.After;
import org.junit.Test;

import java.io.IOException;

import static com.curtcox.snap.model.Packet.ANY;
import static com.curtcox.snap.model.TestClock.tick;
import static org.junit.Assert.*;

public class MultipleNetworkIntegrationTest {
    Runner runner = Runner.of();

    SimpleNetwork lan = SimpleNetwork.newPolling(runner);
    SimpleNetwork internet = SimpleNetwork.newPolling(runner);

    Node inside = Node.on(lan);
    Node outside = Node.on(internet);
    Packet packet = Random.packet();

    @After
    public void stop() {
        runner.stop();
    }

    @Test
    public void packet_written_inside_can_be_read_outside() throws IOException {
        Node.bridge(lan,internet);
        inside.write(packet);
        stall();

        Packet read = outside.read(ANY);
        assertNotNull(read);
        assertEquals(packet,read);
    }

    @Test
    public void packet_written_outside_can_be_read_inside() throws IOException {
        Node.bridge(lan,internet);
        outside.write(packet);
        stall();

        Packet read = inside.read(ANY);
        assertNotNull(read);
        assertEquals(packet,read);
    }

    private void stall() {
        tick(3);
    }

    @Test
    public void packet_will_not_be_sent_back_to_network_it_came_from() throws IOException {
        Node.bridge(lan,internet);
        outside.write(packet);
        stall();

        Packet read = outside.read(ANY);
        assertNull(read);
    }

}
