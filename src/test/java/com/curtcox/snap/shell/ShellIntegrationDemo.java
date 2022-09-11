package com.curtcox.snap.shell;

import com.curtcox.snap.model.*;
import com.curtcox.snap.model.Packet.*;
import com.curtcox.snap.ui.*;
import com.curtcox.snap.ui.Ping;

public class ShellIntegrationDemo {

    final Network network = Snap.newNetwork(Network.Type.memory);
    final SwingShell swingShell = SwingShell.on(network);

    void start() {
        String color = "color";
        String frequency = "frequency";
        String delay = "delay";
        launch("Viewer",    LogViewer.factory);
        launch("Color",     RadioButton.factory, "name","color radio","topic", color,"messages", "ping request,red,green,blue");
        launch("Frequency", RadioButton.factory, "name","freq radio","topic", frequency,"messages", "ping request,90.7,91.9");
        launch("Color",     Display.factory,"name","color display","topic",color);
        launch("Frequency", Display.factory,"name","freq display","topic",frequency);
        launch("Button",    Button.factory,"topic",delay,"message","time 10 topic color message white");
        launch("Timer",     Timer.factory, "topic",delay,"title","Time Left");
        SystemShell.on(network);
        swingShell.init();
        Ping.on(Snap.on(network));
    }

    void launch(String title, UIFrame.ComponentFactory factory, String... args) {
        UIFrame.launch(title,network,factory,args);
    }

    public static void main(String[] args) {
        new ShellIntegrationDemo().start();
    }

}
