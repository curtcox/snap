package com.curtcox.snap.ui;

import com.curtcox.snap.model.*;
import com.curtcox.snap.model.Packet.*;

public class ButtonLogViewerDemo {

    final Network network = Snap.newNetwork(Network.Type.memory);
    final Topic topic = new Topic("/buttons");
    final LogViewer viewer = new LogViewer(topic,Snap.on(network));
    final Button button = new Button(topic,"Button",Snap.on(network));

    void start() {
        viewer.init();
        viewer.show();
        button.init();
        button.show();
    }

    public static void main(String[] args) {
        new ButtonLogViewerDemo().start();
    }
    
}
