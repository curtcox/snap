package com.curtcox.shell;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

final class AwtShell {

    final Frame frame = new Frame();
    final TextArea area = new TextArea();

    final CommandRunner runner;

    AwtShell(CommandRunner runner) {
        this.runner = runner;
    }

    void init() {
        addWindowListener();
        addAreaListener();
        layout();
        show();
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

    private String execute(String command) {
        return runner.execute(command);
    }

    private void outputResult(String result) {
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
                                        frame.dispose();
                                    }
                                }
        );
    }

}