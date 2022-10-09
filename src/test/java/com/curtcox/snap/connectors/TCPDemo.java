package com.curtcox.snap.connectors;

import static com.curtcox.snap.model.Packet.*;
import com.curtcox.snap.model.Snap;
import com.curtcox.snap.ui.*;

import java.io.IOException;

public final class TCPDemo {
    final Network inside = Snap.newNetwork(Network.Type.memory);
    final Network outside = Snap.newNetwork(Network.Type.memory);

    void start() throws IOException {
        inside.add(TCP.newServer());
        outside.add(TCP.newClient(127,0,0,1));
        Flags.Builder flags = new Flags.Builder().topic("button");
        inside(LogViewer.factory,flags.title("Viewer"));
        outside(Button.factory,flags.message("ping request"));
        Ping.on(Snap.on(inside));
    }

    void inside(UIFrame.ComponentFactory factory, Flags.Builder builder) {
        UIFrame.launch(inside,factory,builder.build().args);
    }

    void outside(UIFrame.ComponentFactory factory, Flags.Builder builder) {
        UIFrame.launch(outside,factory,builder.build().args);
    }

    public static void main(String[] args) throws IOException {
        new TCPDemo().start();
    }
}
