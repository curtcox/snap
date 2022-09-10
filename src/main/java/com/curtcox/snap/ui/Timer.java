package com.curtcox.snap.ui;

import com.curtcox.snap.model.Packet;
import com.curtcox.snap.model.Packet.*;
import com.curtcox.snap.model.Snap;

import javax.swing.*;

/**
 * For making a specified announcement after a specified delay.
 */
public final class Timer {

    private Packet packet;
    private int left;
    private final JTextArea textArea = new JTextArea();
    private static final int delay = 1000; //milliseconds
    private final javax.swing.Timer timer = new javax.swing.Timer(delay, evt -> tick());

    private final Snap snap;

    private Timer(Flags flags,Snap snap) {
        this.snap = snap;
        textArea.setEditable(false);
        snap.on(new FilteredSink(filter(flags), packet -> resetUsing(packet)));
    }

    public static final UIFrame.ComponentFactory factory = (flags, snap) -> new Timer(flags,snap).textArea;

    private Filter filter(Flags flags) {
        return packet -> new Topic.Spec(flags.topic()).matches(packet.topic);
    }

    private boolean resetUsing(Packet packet) {
        this.packet = packet;
        timer.start();
        left = duration();
        return true;
    }

    private Flags flags() {
        return new Flags(packet.message.split(" "));
    }

    private int   duration() { return flags().time(); }
    private String message() { return flags().message().replaceAll("_"," "); }
    private Topic    topic() { return flags().topic();}

    private void tick() {
        textArea.setText("" + left);
        left = left - 1;
        if (left<1) {
            end();
        }
    }

    private void end() {
        timer.stop();
        snap.send(topic(),message(),Trigger.from(packet));
    }

    public static void main(String... args) {
        UIFrame.main(factory,args);
    }

}
