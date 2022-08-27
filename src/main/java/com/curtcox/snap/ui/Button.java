package com.curtcox.snap.ui;

import com.curtcox.snap.model.*;

import javax.swing.*;
import java.awt.*;

import com.curtcox.snap.model.Packet.*;

public final class Button {

    final JFrame frame;
    final JButton button;
    final Topic topic;
    final String message;
    final Snap snap;

    Button(Topic topic, String message, Snap snap) {
        this.topic = topic;
        this.message = message;
        frame = new JFrame(topic.toString());
        button = new JButton(message);
        this.snap = snap;
    }

    public static void main(String... args) {
        EventQueue.invokeLater(() -> main0(args));
    }

    static void main0(String[] args) {
        Flags flags = Flags.from(args);
        Topic topic = flags.topic();
        String message = flags.message();
        Snap snap = Snap.newInstance();
        Button button = new Button(topic,message,snap);
        button.init();
        button.show();
    }

    void init() {
        button.addActionListener(e -> onPress());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(button);
        frame.pack();
        frame.doLayout();
    }

    void onPress() {
        snap.send(topic,message);
        System.out.println(topic + " " + message);
    }

    void show() {
        frame.setVisible(true);
    }
}
