package com.curtcox.snap.ui;

import com.curtcox.snap.model.Packet;
import com.curtcox.snap.model.Packet.*;
import com.curtcox.snap.model.Snap;

import javax.swing.*;

public final class Display implements TopicFrame.Factory {
    @Override
    public JComponent newComponent(Flags flags, Snap snap) {
        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        snap.on(new TopicSink(new Topic.Spec(flags.topic()),packet -> { return show(textArea,packet); }));
        return textArea;
    }

    private boolean show(JTextArea textArea,Packet packet) {
        System.out.println("Added " + packet);
        textArea.setText(packet.message);
        return true;
    }

}
