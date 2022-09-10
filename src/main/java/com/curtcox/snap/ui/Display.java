package com.curtcox.snap.ui;

import com.curtcox.snap.model.Packet;
import com.curtcox.snap.model.Packet.*;
import com.curtcox.snap.model.Snap;

import javax.swing.*;

public final class Display {

    private final JTextArea textArea = new JTextArea();

    private Display(Filter filter, Snap snap) {
        textArea.setEditable(false);
        snap.on(new FilteredSink(filter, packet -> show(packet)));
    }

    public static final UIFrame.ComponentFactory factory = (flags, snap) ->
            new Display(packet -> new Topic.Spec(flags.topic()).matches(packet.topic),snap).textArea;

    private boolean show(Packet packet) {
        System.out.println("Display added " + packet);
        textArea.setText(packet.message);
        return true;
    }

}
