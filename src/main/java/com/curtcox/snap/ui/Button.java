package com.curtcox.snap.ui;

import com.curtcox.snap.model.*;

import javax.swing.*;

import com.curtcox.snap.model.Packet.*;

public final class Button extends TopicFrame {

    final String message;
    public Button(Topic topic, String message, Snap snap) {
        super(topic,new JButton(message),snap);
        this.message = message;
    }

    public static void main(String... args) {
        TopicFrame.main((flags, snap) -> new Button(flags.topic(),flags.message(),snap));
    }

    @Override
    void customInit() {
        JButton button = (JButton) component;
        button.addActionListener(e -> onPress());
    }

    void onPress() {
        snap.send(topic,message);
        System.out.println(topic + " " + message);
    }

}
