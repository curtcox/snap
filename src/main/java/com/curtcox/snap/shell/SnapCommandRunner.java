package com.curtcox.snap.shell;

import com.curtcox.snap.model.Snap;

final class SnapCommandRunner implements CommandRunner {

    final Snap snap;
    static final String whoami = "whoami";
    static final String name = "name";

    SnapCommandRunner(Snap snap) {
        this.snap = snap;
    }

    @Override
    public String execute(String command) {
        if (is(whoami,command)) {
            return snap.whoami();
        }
        if (wasNameAssignment(command)) {
            return "";
        }
        return "error : " + command;
    }

    boolean wasNameAssignment(String command) {
        String[] parts = command.split("=");
        if (parts.length==2 && is(name,parts[0])) {
            snap.setName(parts[1].trim());
            return true;
        }
        return false;
    }

    boolean is(String command, String input) {
        return command.equalsIgnoreCase(input.trim());
    }

    @Override
    public String more() {
        return null;
    }
}
