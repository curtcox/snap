package com.curtcox;

import static com.curtcox.Check.notNull;

final class TopicPacketFilter implements Packet.Filter {
    private final String topic;

    TopicPacketFilter(String topic) {
        this.topic = notNull(topic);
    }

    @Override
    public boolean passes(Packet packet) {
        return topic.equals(packet.topic);
    }
}
