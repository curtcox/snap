package com.curtcox.snap.ui;

import com.curtcox.snap.model.*;
import com.curtcox.snap.model.Packet.*;

import javax.swing.*;

public class ButtonLogViewerDemo {

    final Network network = Snap.newNetwork(Network.Type.memory);

    void start() {
        String topic = "button";
        launch("Viewer",new LogViewer());
        launch("Button",new Button(),"topic",topic,"message","Boo!");
        launch("Frequency", new RadioButton(), "topic", topic, "messages", "90.7,91.9");
        Ping.on(new Topic.Spec(topic),Snap.on(network));
    }

    void launch(String title, UIFrame.ComponentFactory factory, String... args) {
        Flags flags = Flags.from(args);
        Snap snap = Snap.on(network);
        JComponent component = factory.newComponent(flags,snap);
        new UIFrame(title,component,flags,snap).launch();
    }

    public static void main(String[] args) {
        new ButtonLogViewerDemo().start();
    }

}
