package com.curtcox.snap.shell;

import com.curtcox.snap.model.*;
import com.curtcox.snap.model.Packet.*;
import com.curtcox.snap.ui.*;
import com.curtcox.snap.ui.Ping;

import javax.swing.*;

public class ShellIntegrationDemo {

    final Network network = Snap.newNetwork(Network.Type.memory);
    final SwingShell swingShell = new SwingShell(new SnapCommandRunner(Snap.on(network)));

    void start() {
        String color = "color";
        String frequency = "frequency";
        launch("Viewer",new LogViewer());
        launch("Color", new RadioButton(), "topic", color,"messages", "red,green,blue");
        launch("Frequency", new RadioButton(), "topic", frequency,"messages", "90.7,91.9");
        launch("Color",new Display(),"topic",color);
        launch("Frequency",new Display(),"topic",frequency);
        swingShell.init();
        Ping.on(new Topic.Spec(new Topic(frequency)),Snap.on(network));
    }

    void launch(String title, UIFrame.ComponentFactory factory, String... args) {
        Flags flags = Flags.from(args);
        Snap snap = Snap.on(network);
        JComponent component = factory.newComponent(flags,snap);
        new UIFrame(title,component,flags,snap).launch();
    }

    public static void main(String[] args) {
        new ShellIntegrationDemo().start();
    }

}
