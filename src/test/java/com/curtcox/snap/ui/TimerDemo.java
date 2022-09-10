package com.curtcox.snap.ui;

import com.curtcox.snap.model.Packet;
import com.curtcox.snap.model.Snap;

public class TimerDemo {

    final Packet.Network network = Snap.newNetwork(Packet.Network.Type.memory);

    void start() {
        String topic = "t";
        launch("Button",    Button.factory,"topic",topic,"message","time 10 topic t2 message ping_request");
        launch("Timer",     new Timer(), "topic",topic,"title","Time Left");
        Ping.on(Snap.on(network));
    }

    void launch(String title, UIFrame.ComponentFactory factory, String... args) {
        UIFrame.launch(title,network,factory,args);
    }

    public static void main(String[] args) {
        new TimerDemo().start();
    }

}