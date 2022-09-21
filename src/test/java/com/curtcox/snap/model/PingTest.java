package com.curtcox.snap.model;

import org.junit.*;

import static org.junit.Assert.*;

public class PingTest {

    @Test
    public void ping_request() {
        Packet packet = packet(Ping.REQUEST);
        assertTrue(Ping.isRequest.test(packet));
        assertFalse(Ping.isResponse.test(packet));
    }

    @Test
    public void ping_response() {
        Packet packet = packet(Ping.RESPONSE);
        assertTrue(Ping.isResponse.test(packet));
        assertFalse(Ping.isRequest.test(packet));
    }

    @Test
    public void empty_packet() {
        Packet packet = packet("");
        assertFalse(Ping.isRequest.test(packet));
        assertFalse(Ping.isResponse.test(packet));
    }

    @Test
    public void random_packet() {
        Packet packet = Random.packet();
        assertFalse(Ping.isRequest.test(packet));
        assertFalse(Ping.isResponse.test(packet));
    }

    private static Packet packet(String message) {
        return Packet.builder()
                .sender(Random.sender())
                .topic(Random.topic())
                .message(message)
                .build();
    }
}
