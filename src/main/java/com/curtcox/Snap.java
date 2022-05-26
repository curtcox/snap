package com.curtcox;

public final class Snap {

    Packet packet;

    public void send(String topic, String message) {
        packet = new Packet(topic,message);
    }

    public Packet listen(String topic) {
        return packet;
    }

}
