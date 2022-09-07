package com.curtcox.snap.ui;

import com.curtcox.snap.model.*;
import com.curtcox.snap.model.Packet.*;

public final class Ping {

    public static void on(Topic.Spec topic, Snap snap) {
        snap.on(new TopicSink(topic,packet -> {
            new Sound().ping();
            return true;
        }));
    }
}
