package com.curtcox.snap.model;

import com.curtcox.snap.connectors.StreamIO;
import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import static com.curtcox.snap.model.Packet.ANY;
import static com.curtcox.snap.model.TestClock.tick;
import static com.curtcox.snap.model.Random.random;
import static org.junit.Assert.*;

public class SimpleNetworkIntegrationTest {

    Runner runner = Runner.of();
    SimpleNetwork network = SimpleNetwork.newPolling(runner);
    Node node1 = Node.on(network);
    Node node2 = Node.on(network);
    Node node3 = Node.on(network);
    Node node4 = Node.on(network);

    Packet.Topic topic = Random.topic();
    String message = random("message");

    Packet.Sender sender = Random.sender();

    Packet packet = new Packet(sender,topic,message);

    @Rule
    public Timeout globalTimeout = Timeout.seconds(3);

    @After
    public void stop() {
        runner.stop();
    }

    @Test
    public void messages_can_be_sent_from_a_node_thru_a_network() throws IOException {
        write_a_packet_to_node_3();

        assertPacket(read(node1));
        assertPacket(read(node2));
        assertPacket(read(node4));
    }

    private Packet read(Node node) throws IOException {
        return node.reader(Packet.ANY).read(Packet.ANY);
    }
    @Test
    public void messages_can_be_sent_from_a_reader_writer_thru_a_network() throws IOException {
        write_a_packet_to_reader_writer();

        assertPacket(read(node1));
        assertPacket(read(node2));
        assertPacket(read(node3));
        assertPacket(read(node4));
    }

    @Test
    public void messages_can_be_read_from_a_reader_writer_thru_a_network() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PacketReaderWriter readerWriter = PacketReaderWriter.from(new StreamIO(new ByteArrayInputStream(new byte[0]),out));
        network.add(readerWriter);
        write_a_packet_to_reader_writer();

        assertPacket(read(node1));
        assertPacket(read(node2));
        assertPacket(read(node3));
        assertPacket(read(node4));
        InputStreamPacketReader reader = new InputStreamPacketReader(new ByteArrayInputStream(out.toByteArray()));
        assertPacket(reader.read(Packet.ANY));
    }

    private void write_a_packet_to_reader_writer() {
        Bytes bytes = packet.asBytes();
        PacketReaderWriter readerWriter = PacketReaderWriter.from(
                new StreamIO(new ByteArrayInputStream(bytes.value()),new ByteArrayOutputStream())
        );
        network.add(readerWriter);
        tick(3);
    }

    private void write_a_packet_to_node_3() {
        node3.write(packet);
        tick(2);
    }

    private void assertPacket(Packet packet) {
        assertNotNull(packet);
        assertEquals(topic,packet.topic);
        assertEquals(message,packet.message);
    }

    @Test
    public void read_returns_packet_after_packet_is_written() throws IOException {
        Packet packet = Random.packet();
        FakeIO io = new FakeIO();

        io.add(packet);
        network.add(io);
        tick(2);

        Packet read = node1.read(ANY);
        assertNotNull(read);
        assertEquals(packet,read);
    }

    @Test
    public void written_packet_can_be_read_from_network() {
        Packet packet = Random.packet();
        FakeIO io = new FakeIO();
        network.add(io);
        node1.write(packet);
        tick(2);

        List<Packet> written = io.written();
        assertEquals(1,written.size());
        assertEquals(packet,written.get(0));
    }

}
