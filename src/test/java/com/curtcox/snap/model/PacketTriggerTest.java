package com.curtcox.snap.model;

import org.junit.*;

import static org.junit.Assert.*;
import static com.curtcox.snap.model.Packet.*;

public class PacketTriggerTest {

    @Test
    public void can_create_from_packet() {
        assertNotNull(Trigger.from(Random.packet()));
    }

    @Test
    public void equal_when_packets_are_the_same() {
        Packet packet = Random.packet();
        assertEquals(Trigger.from(packet),Trigger.from(packet));
        assertEquals(Trigger.from(packet).hashCode(),Trigger.from(packet).hashCode());
    }

    @Test
    public void equal_when_packets_are_equivalent() {
        Packet packet1 = Random.packet();
        Packet packet2 = new Packet(packet1.sender,packet1.topic,packet1.message,packet1.timestamp,packet1.trigger);
        assertEquals(Trigger.from(packet1),Trigger.from(packet2));
        assertEquals(Trigger.from(packet1).hashCode(),Trigger.from(packet2).hashCode());
    }

    @Test
    public void not_equal_when_sender_is_different() {
        Packet packet1 = Random.packet();
        Packet packet2 = Packet.builder()
            .sender(new Packet.Sender(packet1.sender + "2"))
            .topic(packet1.topic)
            .message(packet1.message)
            .timestamp(packet1.timestamp)
            .trigger(packet1.trigger)
            .build();
        assertNotEquals(Trigger.from(packet1),Trigger.from(packet2));
    }

    @Test
    public void not_equal_when_topic_is_different() {
        Packet packet1 = Random.packet();
        Packet packet2 = Packet.builder()
                .sender(packet1.sender)
                .topic(new Packet.Topic(packet1.topic + "2"))
                .message(packet1.message)
                .timestamp(packet1.timestamp)
                .trigger(packet1.trigger)
                .build();
        assertNotEquals(Trigger.from(packet1),Trigger.from(packet2));
    }

    @Test
    public void not_equal_when_message_is_different() {
        Packet packet1 = Random.packet();
        Packet packet2 = new Packet(packet1.sender,packet1.topic,packet1.message + "2",packet1.timestamp,packet1.trigger);
        assertNotEquals(Trigger.from(packet1),Trigger.from(packet2));
    }

    @Test
    public void not_equal_when_timestamp_is_different() {
        Packet packet1 = Random.packet();
        Packet packet2 = new Packet(packet1.sender,packet1.topic,packet1.message,new Timestamp(packet1.timestamp.value+1),packet1.trigger);
        assertNotEquals(Trigger.from(packet1),Trigger.from(packet2));
    }

    @Test
    public void not_equal_when_trigger_is_different() {
        Packet packet1 = Random.packet();
        Packet packet2 = new Packet(
                packet1.sender,packet1.topic,packet1.message,packet1.timestamp,
                Trigger.from(packet1.trigger.toLong() + 1)
        );
        assertNotEquals(Trigger.from(packet1),Trigger.from(packet2));
    }

    @Test
    public void toLong_returns_value_from() {
        long value = System.currentTimeMillis();
        Trigger trigger = Trigger.from(value);
        assertEquals(value,trigger.toLong());
    }
}
