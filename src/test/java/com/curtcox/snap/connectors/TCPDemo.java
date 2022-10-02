package com.curtcox.snap.connectors;

import com.curtcox.snap.model.Packet;
import com.curtcox.snap.model.Snap;
import com.curtcox.snap.ui.*;

import java.io.IOException;

public final class TCPDemo {
    final Packet.Network network = Snap.newNetwork(Packet.Network.Type.memory);

    void start() throws IOException {
        network.add(TCP.newServer());
        Flags.Builder flags = new Flags.Builder().topic("button");
        launch(LogViewer.factory,flags.title("Viewer"));
        launch(Button.factory,flags.message("ping request"));
        Ping.on(Snap.on(network));
    }

    void launch(UIFrame.ComponentFactory factory, Flags.Builder builder) {
        UIFrame.launch(network,factory,builder.build().args);
    }

    public static void main(String[] args) throws IOException {
        new TCPDemo().start();
    }
}
