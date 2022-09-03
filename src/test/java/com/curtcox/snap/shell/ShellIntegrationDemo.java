package com.curtcox.snap.shell;

import com.curtcox.snap.model.*;
import com.curtcox.snap.ui.*;
import com.curtcox.snap.ui.Ping;

public class ShellIntegrationDemo {

    final Packet.Network network = Snap.newNetwork(Packet.Network.Type.memory);
    final Packet.Topic topic = new Packet.Topic("/buttons");
    final LogViewer viewer = new LogViewer(topic,Snap.on(network));
    final Button button = new Button(topic,"Button",Snap.on(network));
    final RadioButton radio = new RadioButton(topic,new String[]{"90.7","91.9"},Snap.on(network));

    final SwingShell swingShell = new SwingShell(new SnapCommandRunner(Snap.on(network)));

    void start() {
        viewer.launch();
        button.launch();
        radio.launch();
        swingShell.init();
        Ping.on(topic,Snap.on(network));
    }

    public static void main(String[] args) {
        new ShellIntegrationDemo().start();
    }

}
