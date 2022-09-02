package com.curtcox.snap.ui;

import com.curtcox.snap.model.*;
import com.curtcox.snap.model.Packet.*;

final class Ping {

    final Topic topic;
    final Snap snap;

    private Ping(Topic topic, Snap snap) {
        this.topic = topic;
        this.snap = snap;
    }

    static Ping on(Topic topic, Snap snap) {
        Ping ping = new Ping(topic,snap);
        snap.on(packet -> {
            new Sound().ping();
            return true;
        });
        return ping;
    }
}
