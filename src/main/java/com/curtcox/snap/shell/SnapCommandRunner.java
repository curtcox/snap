package com.curtcox.snap.shell;

import com.curtcox.snap.model.Snap;

import java.util.LinkedList;

final class SnapCommandRunner implements CommandRunner {

    final Snap snap;
    static final String help = "help";
    static final String whoami = "whoami";
    static final String name = "name";

    final LinkedList<String> moreLines = new LinkedList<>();

    static final String helpText =
            "To see this text type help\n" +
            "whoami -- show the current name of this node.\n" +
            "name   -- set the current name of this node.\n"
    ;


    SnapCommandRunner(Snap snap) {
        this.snap = snap;
        moreLines.add(helpText);
    }

    @Override
    public String execute(String command) {
        if (is(help,command)) {
            return helpText;
        }
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
        return moreLines.isEmpty() ? null : moreLines.removeFirst();
    }
}
