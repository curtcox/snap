package com.curtcox.snap.ui;

import com.curtcox.snap.model.*;

import javax.swing.*;
import java.awt.*;

public final class TopicFrame {

    final JFrame frame;
    final JComponent component;
    final Flags flags;
    final Snap snap;

    public TopicFrame(String title, JComponent component, Flags flags, Snap snap) {
        frame = new JFrame(title);
        this.component = component;
        this.flags = flags;
        this.snap = snap;
    }

    public interface Factory {
        JComponent newComponent(Flags flags, Snap snap);
    }

    static void main(Factory factory, String... args) {
        EventQueue.invokeLater(() -> main0(factory,args));
    }

    static void main0(Factory factory, String[] args) {
        Flags flags = Flags.from(args);
        Snap snap = Snap.newInstance();
        JComponent component = factory.newComponent(flags,snap);
        TopicFrame frame = new TopicFrame(flags.title(),component,flags,snap);
        frame.launch();
    }

    public void launch() {
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
