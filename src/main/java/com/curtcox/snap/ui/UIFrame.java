package com.curtcox.snap.ui;

import com.curtcox.snap.model.*;

import javax.swing.*;
import java.awt.*;

public final class UIFrame {

    final JFrame frame;
    final JComponent component;
    final Flags flags;
    final Snap snap;

    public UIFrame(String title, JComponent component, Flags flags, Snap snap) {
        frame = new JFrame(title);
        this.component = component;
        this.flags = flags;
        this.snap = snap;
    }

    public interface ComponentFactory {
        JComponent newComponent(Flags flags, Snap snap);
    }

    static void main(ComponentFactory factory, String... args) {
        EventQueue.invokeLater(() -> main0(factory,args));
    }

    static void main0(ComponentFactory factory, String[] args) {
        Flags flags = Flags.from(args);
        Snap snap = Snap.newInstance();
        JComponent component = factory.newComponent(flags,snap);
        UIFrame frame = new UIFrame(title(flags,component),component,flags,snap);
        frame.launch();
    }

    private static String title(Flags flags,JComponent component) {
        return flags.title() == null ? component.getClass().getSimpleName() : flags.title();
    }

    public void launch() {
        init();
        show();
    }

    void init() {
        setSnapName();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(component);
        frame.pack();
        frame.doLayout();
    }

    void setSnapName() {
        String name = flags.name();
        String id = name == null ? component.getClass().getSimpleName() : name;
        snap.setName(id + "@" +snap.whoami());
    }

    void show() {
        frame.setVisible(true);
    }

    public static void launch(String title, Packet.Network network, UIFrame.ComponentFactory factory, String... args) {
        Flags flags = Flags.from(args);
        Snap snap = Snap.on(network);
        JComponent component = factory.newComponent(flags,snap);
        new UIFrame(title,component,flags,snap).launch();
    }

}
