package com.curtcox.snap.ui;

import com.curtcox.snap.model.Packet;
import com.curtcox.snap.model.Snap;

public class TimerDemo {

    final Packet.Network network = Snap.newNetwork(Packet.Network.Type.memory);

    void start() {
        Flags.Builder builder = Flags.builder().topic("t");
        launch(Button.factory,builder.message("time 10 topic t2 message ping_request"));
        launch(Timer.factory,builder.title("Time Left"));
        Ping.on(Snap.on(network));
    }

    void launch(UIFrame.ComponentFactory factory, Flags.Builder builder) {
        UIFrame.launch(network,factory,builder.build().args);
    }

    public static void main(String[] args) {
        new TimerDemo().start();
    }

}