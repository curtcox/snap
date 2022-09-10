package com.curtcox.snap.ui;

import com.curtcox.snap.model.*;

import javax.swing.*;

import com.curtcox.snap.model.Packet.*;

/**
 * A button that sends announcements when pressed.
 */
public final class Button  {

    final Snap snap;
    final String message;
    final JButton button;

    private Button(Topic topic, String message, Snap snap) {
        this.message = message;
        this.snap = snap;
        button = new JButton(message);
        button.addActionListener(e -> onPress(topic));
    }

    private void onPress(Topic topic) {
        snap.send(topic,message);
        System.out.println(topic + " " + message);
    }

    public static UIFrame.ComponentFactory factory = (flags, snap) ->
            new Button(flags.topic(), flags.message(),snap).button;

    public static void main(String... args) {
        UIFrame.main(factory,args);
    }


}
