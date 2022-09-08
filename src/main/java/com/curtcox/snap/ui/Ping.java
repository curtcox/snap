package com.curtcox.snap.ui;

import com.curtcox.snap.model.*;
import com.curtcox.snap.model.Packet.*;

public final class Ping {

    public static void on(Filter topic, Snap snap) {
        snap.on(new TopicSink(topic,packet -> {
            new Sound().ping(packet.topic.hashCode());
            return true;
        }));
    }

    public static void on(Snap snap) {
        on(packet -> packet.message.equals(com.curtcox.snap.model.Ping.REQUEST), snap);
    }
}
