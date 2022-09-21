package com.curtcox.snap.ui;

import com.curtcox.snap.model.*;
import com.curtcox.snap.model.Packet.*;

public final class Ping {

    public static void on(Filter topic, Snap snap) {
        snap.on(new FilteredSink(topic, packet -> {
            playPingSound(packet.topic.hashCode());
            return true;
        }));
    }

    private static void playPingSound(int pitch) {
        new Thread(() -> new Sound().ping(pitch)).start();
    }

    public static void on(Snap snap) {
        on(packet -> packet.message.equals(com.curtcox.snap.model.Ping.REQUEST), snap);
    }
}
