package com.curtcox;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.io.IOException;

import static com.curtcox.Random.random;
import static com.curtcox.TestUtil.shortDelay;
import static org.junit.Assert.*;

public class SimpleNetworkIntegrationTest {

    SimpleNetwork network = SimpleNetwork.newPolling();
    Node node1 = Node.on(network);
    Node node2 = Node.on(network);

    String topic = random("topic");
    String message = random("message");

    Packet packet = new Packet(topic,message);

    Node bridge1 = Node.on(network);
    Node bridge2 = Node.on(network);

    @Rule
    public Timeout globalTimeout = Timeout.seconds(3);

    @Test
    public void messages_can_be_sent_thru_a_network() throws IOException {
        write_a_packet_to_the_network();

        assertPacket(bridge2.read());
    }

    @Test
    public void messages_can_be_sent_to_a_network() throws IOException {
        write_a_packet_to_the_network();

        bridge2.read();
        assertPacket(node1.read());
        assertPacket(node2.read());
    }

    private void write_a_packet_to_the_network() throws IOException {
        bridge1.write(packet);
        shortDelay();
    }

    private void assertPacket(Packet packet) {
        assertNotNull(packet);
        assertEquals(topic,packet.topic);
        assertEquals(message,packet.message);
    }
}
