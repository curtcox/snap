package com.curtcox.snap.ui;

import com.curtcox.snap.model.*;
import com.curtcox.snap.model.Packet.*;

public class ButtonLogViewerDemo {

    final Network network = Snap.newNetwork(Network.Type.memory);

    void start() {
        String topic = "button";
        launch("Viewer",new LogViewer());
        launch("Button",    Button.factory,"topic",topic,"message","Boo!");
        launch("Frequency", RadioButton.factory, "topic", topic, "messages", "90.7,91.9");
        Ping.on(Snap.on(network));
    }

    void launch(String title, UIFrame.ComponentFactory factory, String... args) {
        UIFrame.launch(title,network,factory,args);
    }

    public static void main(String[] args) {
        new ButtonLogViewerDemo().start();
    }

}
