package com.curtcox.snap.shell;

import com.curtcox.snap.model.Snap;

import java.awt.*;
import java.awt.event.*;

/**
 * An AWT based shell that can provide a text based UI for a CommandRunner.
 */
final class AwtShell extends CommandShell {

    final Frame frame = new Frame("Snap");
    final TextArea area = new TextArea();

    AwtShell(CommandRunner runner) {
        super(runner);
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

    @Override void outputResult(String result) {
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
        frame.addWindowListener(new WindowAdapter() {
                                    public void windowClosing(WindowEvent we) {
                                        timer.stop();
                                        frame.dispose();
                                    }
                                }
        );
    }

    public static void main(String args[]) {
        new AwtShell(new SnapCommandRunner(Snap.newInstance())).init();
    }

}