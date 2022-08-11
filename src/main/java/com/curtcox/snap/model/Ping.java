package com.curtcox.snap.model;

import java.io.*;

final class Ping {

    final Node node;
    final Snap snap;

    private static final Packet.Topic TOPIC = new Packet.Topic("ping");

    private static final String REQUEST = "request";
    private static final String RESPONSE = "response";

    static final Packet.Filter isPingRequest = packet -> packet.topic.equals(TOPIC) && packet.message.startsWith(REQUEST);
    static final Packet.Filter isPingResponse = packet -> packet.topic.equals(TOPIC) && packet.message.startsWith(RESPONSE);

    Ping(Node node,Snap snap) {
        this.node = node;
        this.snap = snap;
    }

    static void of(Snap snap, Node node, Runner runner) {
        Ping ping = new Ping(node,snap);
        runner.periodically(() -> ping.respondToPingRequests());
    }

    void respondToPingRequests() {
        try {
            Packet.Reader reader = node.read(isPingRequest);
            for (Packet packet = reader.read(isPingRequest); packet !=null; packet = reader.read(isPingRequest)) {
                process(packet);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void process(Packet packet) {
        snap.addPacketToRead(packet);
        respondTo(packet);
    }

    private void respondTo(Packet packet) {
        snap.send(TOPIC,RESPONSE);
    }

    static Packet.Reader ping(Snap snap) {
        snap.send(TOPIC,REQUEST);
        return snap.reader(Ping.isPingResponse);
    }

}
