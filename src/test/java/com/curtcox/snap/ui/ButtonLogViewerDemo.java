package com.curtcox.snap.ui;

import com.curtcox.snap.model.*;
import com.curtcox.snap.model.Packet.*;

public class ButtonLogViewerDemo {

    final Network network = Snap.newNetwork(Network.Type.memory);
    final Topic topic = new Topic("/buttons");
    final LogViewer viewer = new LogViewer(topic,Snap.on(network));
    final Button button = new Button(topic,"Button",Snap.on(network));
    final RadioButton radio = new RadioButton(topic,new String[]{"90.7","91.9"},Snap.on(network));
    final Ping ping = Ping.on(topic,Snap.on(network));

    void start() {
        viewer.launch();
        button.launch();
        radio.launch();
    }

    public static void main(String[] args) {
        new ButtonLogViewerDemo().start();
    }

}
