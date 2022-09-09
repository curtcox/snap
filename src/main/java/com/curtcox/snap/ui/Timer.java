package com.curtcox.snap.ui;

import com.curtcox.snap.model.Packet;
import com.curtcox.snap.model.Snap;

import javax.swing.*;

public final class Timer implements UIFrame.ComponentFactory {
    @Override
    public JComponent newComponent(Flags flags, Snap snap) {
        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        Packet.Filter filter = packet -> new Packet.Topic.Spec(flags.topic()).matches(packet.topic);
        snap.on(new Packet.FilteredSink(filter, packet -> show(textArea,packet)));
        return textArea;
    }

    private boolean show(JTextArea textArea,Packet packet) {
        System.out.println("Display added " + packet);
        textArea.setText(packet.message);
        return true;
    }

    public static void main(String... args) {
        UIFrame.main(new Timer(),args);
    }

}
