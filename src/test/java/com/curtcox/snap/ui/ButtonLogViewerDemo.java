package com.curtcox.snap.ui;

import com.curtcox.snap.model.*;
import com.curtcox.snap.model.Packet.*;

public class ButtonLogViewerDemo {

    public static void main(String[] args) {
        Network network = Snap.newNetwork(Network.Type.memory);
        Topic topic = new Topic("/buttons");
        LogViewer viewer = new LogViewer(topic,Snap.on(network));
        viewer.init();
        viewer.show();
        Button button = new Button(topic,"Button",Snap.on(network));
        button.init();
        button.show();
    }
}
