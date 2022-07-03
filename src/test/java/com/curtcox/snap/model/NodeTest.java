package com.curtcox.snap.model;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static com.curtcox.snap.model.Clock.tick;
import static com.curtcox.snap.model.Random.random;
import static com.curtcox.snap.model.TestUtil.consume;
import static org.junit.Assert.*;

public class NodeTest {

    Node node;

    ReflectorNetwork network = new ReflectorNetwork();

    @Before
    public void setUp() {
        node = Node.on(network);
    }

    @Test
    public void write_should_not_produce_error() {
        node.write(new Packet("Bender","schmopic","smessage"));
    }

    @Test
    public void read_should_return_the_message_sent() throws IOException {
        Packet packet = Random.packet();

        node.write(packet);
        tick(2);
        Packet read = node.read(packet.topic).read();

        assertNotNull(packet);
        assertEquals(packet.sender,read.sender);
        assertEquals(packet.topic,read.topic);
        assertEquals(packet.message,read.message);
    }

    @Test
    public void read_should_only_return_a_message_once_when_topic_specified() throws IOException {
        Packet packet = Random.packet();
        String topic = packet.topic;
        node.write(packet);
        tick(2);
        Packet.Reader reader = node.read(topic);
        assertEquals(packet,reader.read());
        assertNull(node.read(topic).read());
    }

    @Test
    public void read_should_return_a_message_once_when_no_topic_specified() throws IOException {
        node.write(Random.packet());
        tick(2);

        Packet.Reader reader = node.read();
        assertNotNull(reader.read());
        assertNull(reader.read());
    }

    @Test
    public void read_should_return_empty_reader_when_no_messages_sent() throws IOException {
        tick();
        Packet.Reader packets = node.read();

        assertNull(packets.read());
    }

    @Test
    public void read_should_return_null_when_there_is_a_message_that_does_not_match_topic() throws IOException {
        Packet packet = Random.packet();

        node.write(packet);
        tick();
        Packet read = node.read("different " + packet.topic).read();

        assertNull(read);
    }

    @Test
    public void read_with_no_topic_should_return_messages_sent_to_any_topic() throws IOException {
        String topic1 = random("topic1");
        String message1 = random("message1");
        String sender1 = random("sender1");
        node.write(new Packet(sender1,topic1,message1));
        String topic2 = random("topic2");
        String message2 = random("message2");
        String sender2 = random("sender2");
        node.write(new Packet(sender2,topic2,message2));
        tick(3);

        Packet packet1 = consume(node);

        assertNotNull(packet1);
        assertEquals(sender1,packet1.sender);
        assertEquals(topic1,packet1.topic);
        assertEquals(message1,packet1.message);

        Packet packet2 = consume(node);

        assertNotNull(packet2);
        assertEquals(sender2,packet2.sender);
        assertEquals(topic2,packet2.topic);
        assertEquals(message2,packet2.message);
    }

    @Test
    public void read_with_topic_should_return_1st_message_when_it_matches_topic() throws IOException {
        String topic1 = random("topic1");
        String message1 = random("message1");
        String sender1 = random("sender1");
        node.write(new Packet(sender1,topic1,message1));
        node.write(Random.packet());
        tick(2);

        Packet.Reader reader = node.read(topic1);
        Packet packet = reader.read();

        assertNotNull(packet);
        assertEquals(sender1,packet.sender);
        assertEquals(topic1,packet.topic);
        assertEquals(message1,packet.message);

        assertNull(node.read(topic1).read());
    }

    @Test
    public void read_with_topic_should_return_2nd_message_when_it_matches_topic() throws IOException {
        node.write(Random.packet());
        String sender2 = random("sender2");
        String topic2 = random("topic2");
        String message2 = random("message2");
        node.write(new Packet(sender2,topic2,message2));
        tick(3);

        Packet.Reader reader = node.read(topic2);
        Packet packet = reader.read();

        assertNotNull(packet);
        assertEquals(sender2,packet.sender);
        assertEquals(topic2,packet.topic);
        assertEquals(message2,packet.message);
        assertNull(node.read(topic2).read());
    }

    @Test
    public void messsages_are_delivered_in_order_when_on_network() throws IOException {
        Node.on(network);

        node.write(new Packet("s","call","1"));
        node.write(new Packet("s","call","2"));
        node.write(new Packet("s","call","3"));
        node.write(new Packet("s","call","4"));
        tick(5);

        assertEquals("1", consume(node).message);
        assertEquals("2", consume(node).message);
        assertEquals("3", consume(node).message);
        assertEquals("4", consume(node).message);
    }

    @Test
    public void messages_can_be_read_only_once() throws IOException {
        node.write(new Packet("me","phone","ring"));
        tick(2);

        assertNotNull(consume(node));
        assertNull(node.read().read());
    }

}
