package com.curtcox.snap.ui;

import com.curtcox.snap.model.Packet;
import com.curtcox.snap.model.Packet.*;
import com.curtcox.snap.model.Snap;

import javax.swing.*;

/**
 * For making a specified announcement after a specified delay.
 */
public final class Timer implements UIFrame.ComponentFactory {

    private int left;
    private JTextArea textArea = new JTextArea();

    private Packet packet;
    int delay = 1000; //milliseconds
    javax.swing.Timer timer = new javax.swing.Timer(delay, evt -> tick());

    Snap snap;
    @Override
    public JComponent newComponent(Flags flags, Snap snap) {
        textArea.setEditable(false);
        this.snap = snap;
        snap.on(new FilteredSink(filter(flags), packet -> resetUsing(packet)));
        return textArea;
    }

    private Filter filter(Flags flags) {
        return packet -> new Topic.Spec(flags.topic()).matches(packet.topic);
    }

    private boolean resetUsing(Packet packet) {
        this.packet = packet;
        timer.start();
        left = duration(packet);
        return true;
    }

    private Flags flags(Packet packet) {
        return new Flags(packet.message.split(" "));
    }

    private int   duration(Packet packet) { return flags(packet).time(); }
    private String message(Packet packet) { return flags(packet).message().replaceAll("_"," "); }
    private Topic    topic(Packet packet) { return flags(packet).topic();}

    private void tick() {
        textArea.setText("" + left);
        left = left - 1;
        if (left<1) {
            end();
        }
    }

    private void end() {
        timer.stop();
        snap.send(topic(packet),message(packet),Trigger.from(packet));
    }

    public static void main(String... args) {
        UIFrame.main(new Timer(),args);
    }

}
