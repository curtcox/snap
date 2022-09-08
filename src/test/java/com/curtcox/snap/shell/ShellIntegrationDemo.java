package com.curtcox.snap.shell;

import com.curtcox.snap.model.*;
import com.curtcox.snap.model.Packet.*;
import com.curtcox.snap.ui.*;
import com.curtcox.snap.ui.Ping;

public class ShellIntegrationDemo {

    final Network network = Snap.newNetwork(Network.Type.memory);
    final SwingShell swingShell = new SwingShell(new SnapCommandRunner(Snap.on(network)));

    void start() {
        String color = "color";
        String frequency = "frequency";
        launch("Viewer",    new LogViewer());
        launch("Color",     new RadioButton(), "name","color radio","topic", color,"messages", "ping request,red,green,blue");
        launch("Frequency", new RadioButton(), "name","freq radio","topic", frequency,"messages", "ping request,90.7,91.9");
        launch("Color",     new Display(),"name","color display","topic",color);
        launch("Frequency", new Display(),"name","freq display","topic",frequency);
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
