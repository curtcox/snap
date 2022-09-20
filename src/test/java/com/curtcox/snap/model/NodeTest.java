package com.curtcox.snap.model;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static com.curtcox.snap.model.Packet.ANY;
import static com.curtcox.snap.model.Random.random;
import static com.curtcox.snap.model.TestClock.tick;
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
        node.write(Packet.builder()
            .sender(new Packet.Sender("Bender"))
            .topic(new Packet.Topic("schmopic"))
            .message("smessage")
            .build()
        );
    }

    @Test
    public void read_should_return_the_message_sent() throws IOException {
        Packet packet = Random.packet();

        node.write(packet);
        tick(2);
        Packet read = node.reader(new TopicPacketFilter(packet.topic)).read(ANY);

        assertNotNull(packet);
        assertEquals(packet.sender,read.sender);
        assertEquals(packet.topic,read.topic);
        assertEquals(packet.message,read.message);
    }

    @Test
    public void read_should_only_return_a_message_once_when_topic_specified() throws IOException {
        Packet packet = Random.packet();
        Packet.Topic topic = packet.topic;
        node.write(packet);
        tick(2);
        Packet.Filter filter = new TopicPacketFilter(topic);
        Packet.Reader reader = node.reader(filter);
        assertEquals(packet,reader.read(ANY));
        assertNull(node.reader(filter).read(ANY));
    }

    @Test
    public void read_should_return_a_message_once_when_no_topic_specified() throws IOException {
        node.write(Random.packet());
        tick(2);

        Packet.Reader reader = node.reader(ANY);
        assertNotNull(reader.read(ANY));
        assertNull(reader.read(ANY));
    }

    @Test
    public void read_should_return_empty_reader_when_no_messages_sent() throws IOException {
        tick();
        Packet.Reader packets = node.reader(ANY);

        assertNull(packets.read(ANY));
    }

    @Test
    public void read_should_return_null_when_there_is_a_message_that_does_not_match_topic() throws IOException {
        Packet packet = Random.packet();

        node.write(packet);
        tick();
        Packet read = node.reader(new TopicPacketFilter(new Packet.Topic("different " + packet.topic))).read(ANY);

        assertNull(read);
    }

    @Test
    public void read_with_no_topic_should_return_messages_sent_to_any_topic() throws IOException {
        Packet.Topic topic1 = Random.topic();
        String message1 = random("message1");
        Packet.Sender sender1 = Random.sender();
        node.write(Packet.builder().sender(sender1).topic(topic1).message(message1).build());
        Packet.Topic topic2 = Random.topic();
        String message2 = random("message2");
        Packet.Sender sender2 = Random.sender();
        node.write(Packet.builder().sender(sender2).topic(topic2).message(message2).build());
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
        node.write(packet(sender1,topic1,message1));
        node.write(Random.packet());
        tick(2);

        Packet.Reader reader = node.reader(new TopicPacketFilter(new Packet.Topic(topic1)));
        Packet packet = reader.read(ANY);

        assertNotNull(packet);
        assertEquals(sender1,packet.sender.value);
        assertEquals(topic1,packet.topic.value);
        assertEquals(message1,packet.message);

        assertNull(node.reader(new TopicPacketFilter(new Packet.Topic(topic1))).read(ANY));
    }

    @Test
    public void read_with_topic_should_return_2nd_message_when_it_matches_topic() throws IOException {
        node.write(Random.packet());
        String sender2 = random("sender2");
        String topic2 = random("topic2");
        String message2 = random("message2");
        node.write(packet(sender2,topic2,message2));
        tick(3);

        Packet.Reader reader = node.reader(new TopicPacketFilter(new Packet.Topic(topic2)));
        Packet packet = reader.read(ANY);

        assertNotNull(packet);
        assertEquals(sender2,packet.sender.value);
        assertEquals(topic2,packet.topic.value);
        assertEquals(message2,packet.message);
        assertNull(node.reader(new TopicPacketFilter(new Packet.Topic(topic2))).read(ANY));
    }

    @Test
    public void messsages_are_delivered_in_order_when_on_network() throws IOException {
        Node.on(network);

        node.write(packet("s","call","1"));
        node.write(packet("s","call","2"));
        node.write(packet("s","call","3"));
        node.write(packet("s","call","4"));
        tick(5);

        assertEquals("1", consume(node).message);
        assertEquals("2", consume(node).message);
        assertEquals("3", consume(node).message);
        assertEquals("4", consume(node).message);
    }

    @Test
    public void messages_can_be_read_only_once() throws IOException {
        node.write(packet("me","phone","ring"));
        tick(2);

        assertNotNull(consume(node));
        assertNull(node.reader(ANY).read(ANY));
    }

    private Packet packet(String sender,String topic,String message) {
        return Packet.builder()
                .sender(new Packet.Sender(sender))
                .topic(new Packet.Topic(topic))
                .message(message)
                .build();
    }
}
