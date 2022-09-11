package com.curtcox.snap.shell;

import com.curtcox.snap.model.*;
import com.curtcox.snap.model.Packet.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * A Swing based shell that can provide a text based UI for a CommandRunner.
 */
final class SwingShell extends CommandShell {

    final JFrame frame = new JFrame("Snap");
    final JTextArea area = new JTextArea();

    SwingShell(CommandRunner runner) {
        super(runner);
    }

    public static SwingShell on(Network network) {
        return new SwingShell(new SnapCommandRunner(Snap.on(network)));
    }

    void init() {
        addWindowListener();
        addAreaListener();
        layout();
        show();
        startTimer();
    }

    private void addAreaListener() {
        area.addKeyListener(new KeyListener() {
            @Override public void keyTyped(KeyEvent e) { onKeyTyped(e); }
            @Override public void keyPressed(KeyEvent e) {}
            @Override public void keyReleased(KeyEvent e) {}
        });
    }

    private void onKeyTyped(KeyEvent e) {
        if (isEnter(e)) {
            executeLastLine();
        }
    }

    private void executeLastLine() {
        outputResult(execute(lastLine()));
    }

    private String lastLine() {
        String text = area.getText();
        String[] parts = text.split("\n");
        return parts[parts.length - 1];
    }

    void outputResult(String result) {
        area.append(result + "\n");
    }

    private boolean isEnter(KeyEvent e) {
        return e.getKeyChar() == '\n';
    }

    private void show() {
        frame.setVisible(true);
    }

    private void layout() {
        frame.setSize(400, 400);
        frame.setLayout(new BorderLayout());
        frame.add(area,BorderLayout.CENTER);
    }

    private void addWindowListener() {
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
                                    public void windowClosing(WindowEvent we) {
                                        timer.stop();
                                        frame.dispose();
                                    }
                                }
        );
    }

    public static void main(String args[]) {
        new SwingShell(new SnapCommandRunner(Snap.newInstance())).init();
    }

}