package com.curtcox.snap.ui;

import com.curtcox.snap.model.Packet;
import com.curtcox.snap.model.Snap;

public class KeysDemo {

    final Packet.Network network = Snap.newNetwork(Packet.Network.Type.memory);

    void start() {
        Flags.Builder flags = Flags.builder().topic("keys");
        launch(Keys.factory,   flags.name("keys"));
        launch(Display.factory,flags.name("display"));
    }

    void launch(UIFrame.ComponentFactory factory, Flags.Builder builder) {
        UIFrame.launch(network,factory,builder.build().args);
    }

    public static void main(String[] args) {
        new KeysDemo().start();
    }

}
