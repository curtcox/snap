package com.curtcox.snap.model;

import com.curtcox.snap.model.Packet.*;

import java.io.IOException;

/**
 * For determining who can-hear / is-listening-to whom.
 */
final class Ping {

    final Reader.Factory node;
    final Snap snap;

    static final String REQUEST = "ping request";
    static final String RESPONSE = "ping response";

    static final Filter isPingRequest = packet -> REQUEST.equals(packet.message);
    static final Filter isPingResponse = packet -> RESPONSE.equals(packet.message);

    Ping(Packet.Reader.Factory node,Snap snap) {
        this.node = node;
        this.snap = snap;
    }

    static void of(Snap snap, Reader.Factory node, Runner runner) {
        Ping ping = new Ping(node,snap);
        runner.periodically(() -> ping.respondToPingRequests());
    }

    void respondToPingRequests() {
        try {
            Reader reader = node.reader(isPingRequest);
            for (Packet packet = reader.read(isPingRequest); packet !=null; packet = reader.read(isPingRequest)) {
                process(packet);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void process(Packet packet) {
        //snap.addPacketToRead(packet);
        respondTo(packet);
    }

    private void respondTo(Packet packet) {
        snap.send(packet.topic,RESPONSE);
    }

    static Reader ping(Topic topic,Snap snap) {
        snap.send(topic,REQUEST);
        return snap.reader(Ping.isPingResponse);
    }

}
