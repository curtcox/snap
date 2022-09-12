package com.curtcox.snap.ui;

import com.curtcox.snap.model.Packet.Topic;
import com.curtcox.snap.model.Snap;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Map;
import java.util.TreeMap;

/**
 * A button that sends announcements when pressed.
 */
public final class Keys {

    final Snap snap;
    final Topic topic;
    final JTextArea area = new JTextArea();

    private Keys(Topic topic, Snap snap) {
        this.topic = topic;
        this.snap = snap;
        addAreaListener();
    }

    private void addAreaListener() {
        area.addKeyListener(new KeyListener() {
            @Override public void keyTyped(KeyEvent e) { onKeyTyped(e); }
            @Override public void keyPressed(KeyEvent e) {}
            @Override public void keyReleased(KeyEvent e) {}
        });
    }

    private void onKeyTyped(KeyEvent e) {
        snap.send(topic,message(e));
    }

    private String message(KeyEvent e) {
        Map<String,String> map = new TreeMap<>();
        map.put("source","" + e.getSource());
        map.put("id","" + e.getID());
        map.put("when","" + e.getWhen());
        map.put("modifiers","" + e.getModifiersEx());
        map.put("keyCode","" + e.getKeyCode());
        map.put("keyChar",""+e.getKeyChar());
        map.put("keyLocation",""+e.getKeyLocation());
        map.put("isProxyActive",e.paramString());
        return map.toString();
    }

    public static final UIFrame.ComponentFactory factory = (flags, snap) ->
            new Keys(flags.topic(),snap).area;

    public static void main(String... args) {
        UIFrame.main(factory,args);
    }


}
