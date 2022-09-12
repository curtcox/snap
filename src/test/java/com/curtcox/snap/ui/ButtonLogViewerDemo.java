package com.curtcox.snap.ui;

import com.curtcox.snap.model.*;
import com.curtcox.snap.model.Packet.*;

public class ButtonLogViewerDemo {

    final Network network = Snap.newNetwork(Network.Type.memory);

    void start() {
        Flags.Builder flags = new Flags.Builder().topic("button");
        launch(LogViewer.factory,flags.title("Viewer"));
        launch(Button.factory,flags.message("Boo!"));
        launch(RadioButton.factory,flags.messages("90.7,91.9"));
        Ping.on(Snap.on(network));
    }

    void launch(UIFrame.ComponentFactory factory, Flags.Builder builder) {
        UIFrame.launch(network,factory,builder.build().args);
    }

    public static void main(String[] args) {
        new ButtonLogViewerDemo().start();
    }

}
