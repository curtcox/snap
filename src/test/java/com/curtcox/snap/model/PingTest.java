package com.curtcox.snap.model;

import org.junit.*;

import static org.junit.Assert.*;

public class PingTest {

    @Test
    public void can_create() {
        assertNotNull(new Ping(null,null));
    }

    @Test
    public void not_ping_request_packets() {
        isNotPingRequest(topic("",""));
    }

    @Test
    public void not_ping_response_packets() {
        isNotPingResponse(topic("",""));
    }

    @Test
    public void ping_request_packets() {
        isPingRequest(topic("/ping/request",""));
    }

    @Test
    public void ping_response_packets() {
        isPingResponse(topic("/ping/response",""));
    }

    private void isNotPingRequest(Packet packet) {
        assertFalse(Ping.isPingRequest.passes(packet));
    }

    private void isNotPingResponse(Packet packet) {
        assertFalse(Ping.isPingResponse.passes(packet));
    }

    private void isPingRequest(Packet packet) {
        assertTrue(Ping.isPingRequest.passes(packet));
    }

    private void isPingResponse(Packet packet) {
        assertTrue(Ping.isPingResponse.passes(packet));
    }

    private static Packet topic(String topic,String message) {
        return Packet.builder()
                .sender(new Packet.Sender(""))
                .message(message)
                .topic(new Packet.Topic(topic))
                .build();
    }
}
