package com.curtcox.snap.model;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static com.curtcox.snap.model.Clock.tick;
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
        snap = Snap.of(node,runner);
    }

    @Test(expected = NullPointerException.class)
    public void requires_node() {
        assertNotNull(Snap.of(null,Runner.of()));
    }

    @Test(expected = NullPointerException.class)
    public void requires_runner() {
        assertNotNull(Snap.of(Node.on(network),null));
    }

    @Test
    public void write_should_not_produce_error() {
        snap.send(new Packet.Topic("schmopic"),"smessage");
    }

    @Test
    public void read_should_return_the_message_sent() throws IOException {
        Packet.Topic topic = Random.topic();
        String message = random("message");

        snap.send(topic,message);
        tick(2);
        Packet packet = snap.reader(topic).read(ANY);

        assertNotNull(packet);
        assertEquals(topic,packet.topic);
        assertEquals(message,packet.message);
    }

    @Test
    public void read_should_only_return_a_message_once_when_topic_specified() throws IOException {
        Packet.Topic topic = Random.topic();
        snap.send(topic,random("message"));
        tick(2);
        Packet.Reader iterator1 = snap.reader(topic);
        assertNotNull(iterator1.read(ANY));
        Packet.Reader iterator2 = snap.reader(topic);
        assertNull(iterator2.read(ANY));
    }

    @Test
    public void read_should_only_return_a_message_once_when_no_topic_specified() throws IOException {
        snap.send(new Packet.Topic(random("topic")),random("message"));
        tick(2);
        assertNotNull(consume(snap));
        assertNull(snap.reader().read(ANY));
    }

    @Test
    public void read_should_return_null_when_no_messages_sent() throws IOException {
        assertNull(snap.reader().read(ANY));
    }

    @Test
    public void read_should_return_null_when_there_is_a_message_that_does_not_match_topic() throws IOException {
        Packet.Topic topic = Random.topic();
        String message = random("message");

        snap.send(topic,message);
        Packet packet = snap.reader(new Packet.Topic("different " + topic)).read(ANY);

        assertNull(packet);
    }

    @Test
    public void read_with_no_topic_should_return_messages_sent_to_any_topic() throws IOException {
        Packet.Topic topic1 = Random.topic();
        String message1 = random("message1");
        snap.send(topic1,message1);
        Packet.Topic topic2 = Random.topic();
        String message2 = random("message2");
        snap.send(topic2,message2);
        tick(3);

        Packet.Reader packets = snap.reader();

        Packet packet1 = packets.read(ANY);

        assertNotNull(packet1);
        assertEquals(topic1,packet1.topic);
        assertEquals(message1,packet1.message);

        Packet packet2 = packets.read(ANY);

        assertNotNull(packet2);
        assertEquals(topic2,packet2.topic);
        assertEquals(message2,packet2.message);
    }

    @Test
    public void read_with_topic_should_return_1st_message_when_it_matches_topic() throws IOException {
        Packet.Topic topic1 = Random.topic();
        String message1 = random("message1");
        snap.send(topic1,message1);
        Packet.Topic topic2 = Random.topic();
        String message2 = random("message2");
        snap.send(topic2,message2);
        tick(2);

        Packet.Reader iterator = snap.reader(topic1);
        Packet packet = iterator.read(ANY);

        assertNotNull(packet);
        assertEquals(topic1,packet.topic);
        assertEquals(message1,packet.message);

        assertNull(snap.reader(topic1).read(ANY));
    }

    @Test
    public void read_with_topic_should_return_2nd_message_when_it_matches_topic() throws IOException {
        Packet.Topic topic1 = Random.topic();
        String message1 = random("message1");
        snap.send(topic1,message1);
        Packet.Topic topic2 = Random.topic();
        String message2 = random("message2");
        snap.send(topic2,message2);
        tick(3);

        Packet.Reader iterator = snap.reader(topic2);
        Packet packet = iterator.read(ANY);

        assertNotNull(packet);
        assertEquals(topic2,packet.topic);
        assertEquals(message2,packet.message);
        assertNull(snap.reader(topic2).read(ANY));
    }

    @Test
    public void messsages_are_delivered_in_order_when_on_network() throws IOException {
        Node.on(network);
        Packet.Topic topic = new Packet.Topic("call");

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
        Reader reader = snap.ping(topic);
        tick(3);

        Packet request = reader.read(ANY);
        assertNotNull(request);
        assertTrue(Ping.isPingRequest.passes(request));
        assertEquals(snap.whoami(),request.sender.value);

        Packet response = reader.read(ANY);
        assertNotNull(response);
        assertTrue(Ping.isPingResponse.passes(response));
        assertEquals(snap.whoami(),response.sender.value);
    }


    @Test
    public void ping_returns_ping_responses_on_simple_network() throws Exception {
        SimpleNetwork network = SimpleNetwork.newPolling();
        Snap responder = Snap.on(network);
        String responderName = random("responder");
        responder.setName(responderName);

        Topic topic = Random.topic();
        Reader responses = Snap.on(network).ping(topic);
        tick(3);

        Packet packet = responses.read(ANY);
        assertNotNull(packet);
        assertTrue(Ping.isPingResponse.passes(packet));
        assertEquals(responderName,packet.sender.value);
    }

    @Test
    public void ping_request_can_be_read_by_other_nodes() throws Exception {
        SimpleNetwork network = SimpleNetwork.newPolling();
        Snap snap1 = Snap.on(network);
        Snap snap2 = Snap.on(network);
        Topic topic = Random.topic();

        snap1.ping(topic);
        tick(3);

        Packet packet = snap2.reader().read(ANY);
        assertNotNull(packet);
        assertTrue(Ping.isPingRequest.passes(packet));
        assertEquals(snap1.whoami(),packet.sender.value);
    }

}
