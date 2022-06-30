package com.curtcox;

final class TopicPacketFilter implements Packet.Filter {
    TopicPacketFilter(String topic) {

    }

    @Override
    public boolean passes(Packet packet) {
        return false;
    }
}
