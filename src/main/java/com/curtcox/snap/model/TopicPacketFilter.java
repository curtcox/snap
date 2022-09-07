package com.curtcox.snap.model;

import static com.curtcox.snap.util.Check.notNull;
import com.curtcox.snap.model.Packet.*;

final class TopicPacketFilter implements Filter {
    private final Packet.Topic topic;

    TopicPacketFilter(Topic topic) {
        this.topic = notNull(topic);
    }

    @Override
    public boolean passes(Packet packet) {
        return topic.equals(packet.topic);
    }
}
