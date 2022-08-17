package com.curtcox.snap.model;

import java.net.*;

import static com.curtcox.snap.util.Check.notNull;

// Potential future API
public final class Snap implements Packet.Reader.Factory {

    final Node node;

    private String name;

    private final ConcurrentPacketList toRead = new ConcurrentPacketList();

    private Snap(Node node) {
        this.node = notNull(node);
    }

    public static Snap newInstance() {
        return on(newNetwork(null));
    }

    public static Packet.Network newNetwork(Packet.Network.Type type) {
        return SimpleNetwork.newPolling();
    }

    public static Snap on(Packet.Network network) {
        return Snap.of(Node.on(network),Runner.of());
    }

    public static Snap of(Node node, Runner runner) {
        Snap snap = new Snap(node);
        Ping.of(snap,node,runner);
        return snap;
    }

    public void send(Packet.Topic topic, String message) {
        node.write(Packet.builder()
                .sender(new Packet.Sender(whoami()))
                .topic(topic)
                .message(message)
                .build()
        );
    }

    public Packet.Reader listen(Packet.Topic topic) {
        return reader(new TopicPacketFilter(topic));
    }

    public Packet.Reader listen() {
        return reader(Packet.ANY);
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
        return Ping.ping(this);
    }

    @Override
    public Packet.Reader reader(Packet.Filter filter) {
        return new CombinedReader(toRead,node.reader(filter));
    }

    void addPacketToRead(Packet packet) {
        toRead.add(packet);
    }
}
