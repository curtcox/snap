package com.curtcox.snap.shell;

import java.util.Date;

final class EchoCommandRunner implements CommandRunner {

    private long lastMessage = now();

    @Override
    public String execute(String command) {
        return "Did " + command;
    }

    @Override
    public String more() {
        if (longEnoughSinceLastMessage()) {
            lastMessage = now();
            return new Date().toString();
        }
        return null;
    }

    private boolean longEnoughSinceLastMessage() {
        long interval = 60 * 1000;
        return now() - lastMessage > interval;
    }

    private static long now() {
        return System.currentTimeMillis();
    }
}
