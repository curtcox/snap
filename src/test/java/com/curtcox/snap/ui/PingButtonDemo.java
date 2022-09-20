package com.curtcox.snap.ui;

import com.curtcox.snap.model.Packet;
import com.curtcox.snap.model.Snap;

public class PingButtonDemo {
    final Packet.Network network = Snap.newNetwork(Packet.Network.Type.memory);

    void start() {
        Flags.Builder flags = new Flags.Builder().topic("button");
        launch(LogViewer.factory,flags.title("Viewer"));
        launch(Button.factory,flags.message("ping request"));
        Ping.on(Snap.on(network));
    }

    void launch(UIFrame.ComponentFactory factory, Flags.Builder builder) {
        UIFrame.launch(network,factory,builder.build().args);
    }

    public static void main(String[] args) {
        new PingButtonDemo().start();
    }

}
