package com.curtcox;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import static com.curtcox.Random.random;
import static org.junit.Assert.*;

public class NetworkIntegrationTest {

    Network network = new Network();
    Node node1 = Node.on(network);
    Node node2 = Node.on(network);

    String topic = random("topic");
    String message = random("message");

    Packet packet = new Packet(topic,message);
    PipedOutputStream externalInput = new PipedOutputStream();
    PipedInputStream externalOutput = new PipedInputStream();
    OutputStreamPacketWriter externalWriter = new OutputStreamPacketWriter(externalInput);
    InputStreamPacketReader externalReader = new InputStreamPacketReader(externalOutput);


    @Before
    public void setUp() throws IOException {
        PacketRelay relay = new PacketRelay(
                new InputStreamPacketReader(new PipedInputStream(externalInput)),
                new OutputStreamPacketWriter(new PipedOutputStream(externalOutput))
        );
        network.add(relay);
    }

    @Rule
    public Timeout globalTimeout = Timeout.seconds(2);

    @Test
    public void messages_can_be_sent_thru_a_network() throws IOException {
        externalWriter.write(packet);
        assertPacket(externalReader.read());
    }

    @Test
    public void messages_can_be_sent_to_a_network() throws IOException {
        externalWriter.write(packet);

        assertPacket(node1.listen());
        assertPacket(node2.listen());
    }

    private void assertPacket(Packet packet) {
        assertNotNull(packet);
        assertEquals(topic,packet.topic);
        assertEquals(message,packet.message);
    }
}