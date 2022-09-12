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
        launch(LogViewer.factory,   flags());
        launch(RadioButton.factory, flags().topic(color).name("color radio").messages("ping request,red,green,blue"));
        launch(RadioButton.factory, flags().topic(frequency).name("freq radio").messages("ping request,90.7,91.9"));
        launch(Display.factory,     flags().topic(color).name("color display"));
        launch(Display.factory,     flags().topic(frequency).name("freq display"));
        launch(Button.factory,      flags().topic(delay).message("time 10 topic color message white"));
        launch(Timer.factory,       flags().topic(delay).title("Time Left"));
        SystemShell.on(network);
        swingShell.init();
        Ping.on(Snap.on(network));
    }

    Flags.Builder flags() {
        return Flags.builder();
    }

    void launch(UIFrame.ComponentFactory factory, Flags.Builder flags) {
        UIFrame.launch(network,factory,flags.build().args);
    }

    public static void main(String[] args) {
        new ShellIntegrationDemo().start();
    }

}
