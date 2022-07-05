package com.curtcox.snap.model;

import java.io.IOException;
import java.net.*;

import static com.curtcox.snap.util.Check.notNull;

// Potential future API
public final class Snap {

    final Node node;

    final Runner runner;
    private String name;

    private static final String PING = "ping";
    private static final String REQUEST = "request";
    private static final String RESPONSE = "response";

    private Snap(Node node, Runner runner) {
        this.node = notNull(node);
        this.runner = notNull(runner);
    }

    public static Snap newInstance() {
        throw new RuntimeException();
        //return null;
    }

    public static Packet.Network newNetwork(Packet.Network.Type type) {
        return SimpleNetwork.newPolling();
    }

    public static Snap on(Packet.Network network) {
        return Snap.of(Node.on(network),Runner.of());
    }

    public static Snap of(Node node, Runner runner) {
        Snap snap = new Snap(node,runner);
        runner.periodically(() -> snap.respondToPingRequests());
        return snap;
    }

    private void respondToPingRequests() {
        try {
            Packet.Reader reader = node.read(packet -> packet.topic.equals(PING) && packet.message.startsWith(REQUEST));
            for (Packet packet = reader.read(); packet !=null; packet = reader.read()) {
                respondTo(packet);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void respondTo(Packet packet) {
        send(PING,RESPONSE);
    }

    public void send(String topic, String message) {
        node.write(new Packet(whoami(),topic,message));
    }

    public Packet.Reader listen(String topic) {
        return node.read(new TopicPacketFilter(topic));
    }

    public Packet.Reader listen() {
        return node.read();
    }


    public String whoami() {
        if (name==null) {
            return defaultName();
        }
        return name;
    }

    private String defaultName() {
        return user() + "@" + host();
    }

    public String user() {
        return System.getProperty("user.name");
    }

    public String host() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public Packet.Reader ping() {
        send(PING,REQUEST);
        return node.read(packet -> packet.topic.equals(PING) && packet.message.startsWith(RESPONSE));
    }
}
