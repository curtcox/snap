package com.curtcox.snap.shell;

import com.curtcox.snap.model.Snap;

final class SnapCommandRunner implements CommandRunner {

    final Snap snap;
    static final String whoami = "whoami";

    SnapCommandRunner(Snap snap) {
        this.snap = snap;
    }

    @Override
    public String execute(String command) {
        if (is(whoami,command)) {
            return snap.whoami();
        }
        return null;
    }

    boolean is(String command, String input) {
        return command.equalsIgnoreCase(input.trim());
    }

    @Override
    public String more() {
        return null;
    }
}
