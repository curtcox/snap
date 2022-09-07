package com.curtcox.snap.ui;

import com.curtcox.snap.model.*;

import javax.swing.*;

import com.curtcox.snap.model.Packet.*;

public final class Button implements UIFrame.ComponentFactory {

    Snap snap;
    String message;
    @Override
    public JComponent newComponent(Flags flags,Snap snap) {
        this.snap = snap;
        message = flags.message();
        JButton button = new JButton(message);
        button.addActionListener(e -> onPress(flags.topic()));
        return button;
    }

    public static void main(String... args) {
        UIFrame.main(new Button(),args);
    }

    void onPress(Topic topic) {
        snap.send(topic,message);
        System.out.println(topic + " " + message);
    }

}
