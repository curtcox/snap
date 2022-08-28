package com.curtcox.snap.model;

import java.net.*;

import static com.curtcox.snap.util.Check.notNull;
import com.curtcox.snap.model.Packet.*;

// Potential future API
public final class Snap implements Reader.Factory, Sink.Acceptor {

    final Node node;

    private String name;

//    private final ConcurrentPacketList toRead = new ConcurrentPacketList();

    private Snap(Node node) {
        this.node = notNull(node);
    }

    public static Snap newInstance() {
        return on(newNetwork(null));
    }

    public static Network newNetwork(Network.Type type) {
        return SimpleNetwork.newPolling();
    }

    public static Snap on(Network network) {
        return Snap.of(Node.on(network),Runner.of());
    }

    public static Snap of(Node node, Runner runner) {
        Snap snap = new Snap(node);
        Ping.of(snap,node,runner);
        return snap;
    }

    public void send(Topic topic, String message) {
        node.write(Packet.builder()
                .sender(new Packet.Sender(whoami()))
                .topic(topic)
                .message(message)
                .build()
        );
    }

    public Reader reader(Topic topic) {
        return reader(new TopicPacketFilter(topic));
    }

    @Override
    public void on(Sink sink) {
        node.on(sink);
    }

    public Reader reader() {
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

    public Reader ping(Topic topic) {
        return Ping.ping(topic,this);
    }

    @Override
    public Reader reader(Packet.Filter filter) {
        return node.reader(filter);
//        return new CombinedReader(toRead,node.reader(filter));
    }

//    void addPacketToRead(Packet packet) {
//        toRead.add(packet);
//    }
}
