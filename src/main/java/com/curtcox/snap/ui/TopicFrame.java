package com.curtcox.snap.ui;

import com.curtcox.snap.model.*;

import javax.swing.*;
import java.awt.*;

import com.curtcox.snap.model.Packet.*;

abstract class TopicFrame {

    final JFrame frame;
    final JComponent component;
    final Topic topic;
    final Snap snap;

    TopicFrame(Topic topic, JComponent component, Snap snap) {
        this.topic = topic;
        frame = new JFrame(topic.toString());
        this.component = component;
        this.snap = snap;
    }

    interface Factory {
        TopicFrame from(Flags flags,Snap snap);
    }

    static void main(Factory factory, String... args) {
        EventQueue.invokeLater(() -> main0(factory,args));
    }

    static void main0(Factory factory, String[] args) {
        Flags flags = Flags.from(args);
        Snap snap = Snap.newInstance();
        factory.from(flags,snap).launch();
    }

    void launch() {
        init();
        show();
    }

    void init() {
        customInit();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(component);
        frame.pack();
        frame.doLayout();
    }

    void customInit() {}

    void show() {
        frame.setVisible(true);
    }
}
