package com.curtcox.snap.model;

import org.junit.*;

import static org.junit.Assert.*;

public class PacketTriggerTest {

    @Test
    public void can_create_from_packet() {
        assertNotNull(Packet.Trigger.from(Random.packet()));
    }

    @Test
    public void equal_when_packets_are_the_same() {
        Packet packet = Random.packet();
        assertEquals(Packet.Trigger.from(packet),Packet.Trigger.from(packet));
        assertEquals(Packet.Trigger.from(packet).hashCode(),Packet.Trigger.from(packet).hashCode());
    }

    @Test
    public void equal_when_packets_are_equivalent() {
        Packet packet1 = Random.packet();
        Packet packet2 = new Packet(packet1.sender,packet1.topic,packet1.message,packet1.timestamp,packet1.trigger);
        assertEquals(Packet.Trigger.from(packet1),Packet.Trigger.from(packet2));
        assertEquals(Packet.Trigger.from(packet1).hashCode(),Packet.Trigger.from(packet2).hashCode());
    }

    @Test
    public void not_equal_when_sender_is_different() {
        Packet packet1 = Random.packet();
        Packet packet2 = new Packet(packet1.sender + "2",packet1.topic,packet1.message,packet1.timestamp,packet1.trigger);
        assertNotEquals(Packet.Trigger.from(packet1),Packet.Trigger.from(packet2));
    }

    @Test
    public void not_equal_when_topic_is_different() {
        Packet packet1 = Random.packet();
        Packet packet2 = new Packet(packet1.sender,packet1.topic + "2",packet1.message,packet1.timestamp,packet1.trigger);
        assertNotEquals(Packet.Trigger.from(packet1),Packet.Trigger.from(packet2));
    }

    @Test
    public void not_equal_when_message_is_different() {
        Packet packet1 = Random.packet();
        Packet packet2 = new Packet(packet1.sender,packet1.topic,packet1.message + "2",packet1.timestamp,packet1.trigger);
        assertNotEquals(Packet.Trigger.from(packet1),Packet.Trigger.from(packet2));
    }

    @Test
    public void not_equal_when_timestamp_is_different() {
        Packet packet1 = Random.packet();
        Packet packet2 = new Packet(packet1.sender,packet1.topic,packet1.message,packet1.timestamp+1,packet1.trigger);
        assertNotEquals(Packet.Trigger.from(packet1),Packet.Trigger.from(packet2));
    }

    @Test
    public void not_equal_when_trigger_is_different() {
        Packet packet1 = Random.packet();
        Packet packet2 = new Packet(
                packet1.sender,packet1.topic,packet1.message,packet1.timestamp,
                Packet.Trigger.from(packet1.trigger.toLong() + 1)
        );
        assertNotEquals(Packet.Trigger.from(packet1),Packet.Trigger.from(packet2));
    }

    @Test
    public void toLong_returns_value_from() {
        long value = System.currentTimeMillis();
        Packet.Trigger trigger = Packet.Trigger.from(value);
        assertEquals(value,trigger.toLong());
    }
}
