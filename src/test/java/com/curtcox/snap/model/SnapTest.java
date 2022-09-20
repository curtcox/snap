package com.curtcox.snap.model;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static com.curtcox.snap.model.TestClock.tick;
import static com.curtcox.snap.model.Random.random;
import static com.curtcox.snap.model.TestUtil.consume;
import static com.curtcox.snap.model.Packet.*;

import static org.junit.Assert.*;

public class SnapTest {

    Snap snap;

    Node node;

    Runner runner = Runner.of();

    ReflectorNetwork network = new ReflectorNetwork();

    @Before
    public void setUp() {
        node = Node.on(network);
        snap = Snap.of(node);
    }

    @Test(expected = NullPointerException.class)
    public void requires_node() {
        assertNotNull(Snap.of(null));
    }

    @Test
    public void write_should_not_produce_error() {
        snap.send(new Packet.Topic("schmopic"),"smessage");
    }

    static class TopicMessage {
        final Topic topic;
        final String message;

        TopicMessage(Topic topic, String message) {
            this.topic = topic;
            this.message = message;
        }
    }

    private TopicMessage sendRandom(String prefix) {
        Topic topic = Random.topic();
        String message = random(prefix);
        snap.send(topic,message);
        return new TopicMessage(topic,message);
    }

    private void assertMatch(TopicMessage topicMessage, Packet packet) {
        assertNotNull(packet);
        assertEquals(topicMessage.topic,packet.topic);
        assertEquals(topicMessage.message,packet.message);
    }

    @Test
    public void read_should_return_the_message_sent() throws IOException {
        TopicMessage sent = sendRandom("message");

        tick(2);
        Packet packet = consume(snap,sent.topic);

        assertMatch(sent,packet);
    }

    @Test
    public void read_should_only_return_a_message_once_when_topic_specified() throws IOException {
        TopicMessage sent = sendRandom("message");

        tick(2);

        assertMatch(sent,consume(snap));
        assertNull(consume(snap));
    }

    @Test
    public void read_should_only_return_a_message_once_when_no_topic_specified() throws IOException {
        snap.send(new Topic(random("topic")),random("message"));
        tick(2);
        assertNotNull(consume(snap));
        assertNull(consume(snap));
    }

    @Test
    public void read_should_return_null_when_no_messages_sent() throws IOException {
        assertNull(consume(snap));
    }

    @Test
    public void read_should_return_null_when_there_is_a_message_that_does_not_match_topic() throws IOException {
        Topic topic = Random.topic();
        String message = random("message");

        snap.send(topic,message);
        Packet packet = snap.reader(new Topic("different " + topic)).read(ANY);

        assertNull(packet);
    }

    @Test
    public void read_with_no_topic_should_return_messages_sent_to_any_topic() throws IOException {
        TopicMessage sent1 = sendRandom("message1");
        TopicMessage sent2 = sendRandom("message2");
        tick(3);

        Packet.Reader packets = snap.reader();

        assertMatch(sent1,packets.read(ANY));
        assertMatch(sent2,packets.read(ANY));
    }

    @Test
    public void read_with_topic_should_return_1st_message_when_it_matches_topic() throws IOException {
        TopicMessage sent1 = sendRandom("message1");
        sendRandom("message2");
        tick(2);

        assertMatch(sent1,consume(snap,sent1.topic));
        assertNull(snap.reader(sent1.topic).read(ANY));
    }

    @Test
    public void read_with_topic_should_return_2nd_message_once_when_it_matches_topic() throws IOException {
        sendRandom("message1");
        TopicMessage sent2 = sendRandom("message2");

        tick(3);

        assertMatch(sent2,consume(snap,sent2.topic));
        assertNull(consume(snap,sent2.topic));
    }

    @Test
    public void messages_are_delivered_in_order_when_on_network() throws IOException {
        Node.on(network);
        Topic topic = new Topic("call");

        snap.send(topic,"1");
        snap.send(topic,"2");
        snap.send(topic,"3");
        snap.send(topic,"4");
        tick(5);

        Packet.Reader packets = snap.reader();

        assertEquals("1", packets.read(ANY).message);
        assertEquals("2", packets.read(ANY).message);
        assertEquals("3", packets.read(ANY).message);
        assertEquals("4", packets.read(ANY).message);
    }

    @Test
    public void snap_returns_ping_response_on_reflector_network() throws Exception {
        Node.on(network);

        Topic topic = Random.topic();
        SinkReader reader = new SinkReader();
        snap.ping(topic,reader);
        tick(3);

        Packet request = reader.read(ANY);
        assertNotNull(request);
        assertEquals(snap.whoami(),request.sender.value);

        Packet response = reader.read(ANY);
        assertNotNull(response);
        assertEquals(snap.whoami(),response.sender.value);
    }


    @Test
    public void ping_returns_ping_responses_on_simple_network() throws Exception {
        SimpleNetwork network = SimpleNetwork.newPolling();
        Snap responder = Snap.on(network);
        String responderName = random("responder");
        responder.setName(responderName);

        Topic topic = Random.topic();
        SinkReader responses = new SinkReader();
        Snap.on(network).ping(topic,responses);
        tick(3);

        Packet packet = responses.read(ANY);
        assertNotNull(packet);
        assertEquals(responderName,packet.sender.value);
    }

    @Test
    public void ping_request_can_be_read_by_other_nodes() throws Exception {
        SimpleNetwork network = SimpleNetwork.newPolling();
        Snap snap1 = Snap.on(network);
        Snap snap2 = Snap.on(network);
        Topic topic = Random.topic();

        snap1.ping(topic,new SinkReader());
        tick(3);

        Packet packet = consume(snap2);
        assertNotNull(packet);
        assertEquals(snap1.whoami(),packet.sender.value);
    }

}
