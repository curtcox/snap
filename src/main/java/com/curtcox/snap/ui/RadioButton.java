package com.curtcox.snap.ui;

import com.curtcox.snap.model.*;

import javax.swing.*;

import com.curtcox.snap.model.Packet.*;

public final class RadioButton extends TopicFrame {

    final String[] messages;
    public RadioButton(Topic topic, String[] messages, Snap snap) {
        super(topic,new RadioButtonPanel(topic,messages,snap),snap);
        this.messages = messages;
    }

    static class RadioButtonPanel extends JPanel {

        final ButtonGroup group = new ButtonGroup();
        final Snap snap;
        final Topic topic;

        public RadioButtonPanel(Topic topic, String[] messages, Snap snap) {
            this.topic = topic;
            this.snap = snap;
            for (String message : messages) {
                JRadioButton button = new JRadioButton(message);
                button.addActionListener(e -> onPress(message));
                group.add(button);
                add(button);
            }
        }
        void onPress(String message) {
            snap.send(topic,message);
            System.out.println(topic + " " + message);
        }
    }

    public static void main(String... args) {
        TopicFrame.main((flags, snap) -> new RadioButton(flags.topic(),flags.messages(),snap));
    }

}
