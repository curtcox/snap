package com.curtcox.snap.model;

import java.net.*;

import static com.curtcox.snap.util.Check.*;
import com.curtcox.snap.model.Packet.*;

// Potential future API
public final class Snap implements Reader.Factory, Sink.Acceptor {

    private final Node node;

    private String name;

    private final int id;

    private static int counter;

    private static final boolean debug = Debug.on;

    private Snap(Node node,int id) {
        this.node = notNull(node);
        this.id = id;
    }

    public static Snap newInstance() {
        return on(newNetwork(null));
    }

    public static Network newNetwork(Network.Type type) {
        return SimpleNetwork.newPolling();
    }

    public static Snap on(Network network) { return Snap.of(Node.on(network)); }
    public static Snap namedOn(String name, Network network) {
        Snap snap = Snap.of(Node.on(network));
        snap.setName(name+"@"+snap.whoami());
        return snap;
    }

    public static Snap of(final Node node) {
        counter++;
        Snap snap = new Snap(node,counter);
        node.on(packet -> {
            snap.sendAutomaticResponses(packet);
            return true;
        });
        return snap;
    }

    private void sendAutomaticResponses(Packet packet) {
        automaticPingResponse(packet);
        automaticDuplicateSenderNotification(packet);
    }

    private void automaticPingResponse(Packet packet) {
        if (Ping.isRequest.test(packet)) {
            if (debug) debug(name + " responding to " + packet);
            sendPingResponse(packet);
        } else {
            //debug(name + " ignoring " + packet);
        }
    }

    private static boolean isDupNotice(Packet packet) {
        return new Topic.Spec(Topic.Duplicate_Sender_Notification).matches(packet.topic);
    }

    private void automaticDuplicateSenderNotification(Packet packet) {
        if (packet.sender.value.equals(name) && !isDupNotice(packet)) {
            reportDuplicateSender(packet);
        }
    }

    public void send(Topic topic, String message) {
        send(Packet.builder()
                .sender(new Packet.Sender(whoami()))
                .topic(topic)
                .message(message)
                .build()
        );
    }

    public void send(Topic topic, String message,Trigger trigger) {
        send(Packet.builder()
                .sender(new Packet.Sender(whoami()))
                .topic(topic)
                .message(message)
                .trigger(trigger)
                .build()
        );
    }

    void send(Packet packet) {
        node.write(packet);
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
        return id + "@" + user() + "@" + host();
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

    public void ping(Topic topic,Sink sink) {
        node.on(sink);
        send(topic,Ping.REQUEST);
    }

    private void sendPingResponse(Packet packet) {
        send(Packet.builder()
                .sender(new Packet.Sender(whoami()))
                .topic(packet.topic)
                .message(Ping.RESPONSE)
                .trigger(Trigger.from(packet))
                .build()
        );
    }

    private void reportDuplicateSender(Packet packet) {
        send(Packet.builder()
                .sender(new Packet.Sender(whoami()))
                .topic(Topic.Duplicate_Sender_Notification)
                .message("Duplicate sender named : " + packet.sender)
                .trigger(Trigger.from(packet))
                .build()
        );
    }

    @Override
    public Reader reader(Packet.Filter filter) {
        return node.reader(filter);
    }

    private static void debug(String message) {
        System.out.println("Snap " + message);
    }

}
