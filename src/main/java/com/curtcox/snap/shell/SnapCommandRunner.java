package com.curtcox.snap.shell;

import com.curtcox.snap.model.Packet;
import com.curtcox.snap.model.Snap;

import java.util.LinkedList;
import static com.curtcox.snap.model.Packet.*;

final class SnapCommandRunner implements CommandRunner {

    final Snap snap;
    static final String help = "help";
    static final String whoami = "whoami";
    static final String name = "name";
    static final String monitor = "monitor";

    final LinkedList<String> moreLines = new LinkedList<>();

    private Topic.Spec monitorTopic;

    private final Sink sinkMonitor = new Sink() {

        @Override
        public boolean add(Packet packet) {
            if (monitorTopic!=null && packet.topic.matches(monitorTopic)) {
                moreLines.add(packet.toString());
                return true;
            }
            return false;
        }
    };

    static final String helpText =
            "To see this text type help\n" +
            "whoami          -- show the current name of this node.\n" +
            "name            -- set the current name of this node.\n" +
            "monitor {topic} -- show messages posted to the given topic.\n"
    ;


    SnapCommandRunner(Snap snap) {
        this.snap = snap;
        moreLines.add(helpText);
        snap.on(sinkMonitor);
    }

    @Override
    public String execute(String command) {
        if (is(help,command)) {
            return helpText;
        }
        if (is(whoami,command)) {
            return snap.whoami();
        }
        if (wasMonitorAssignment(command)) {
            return "";
        }
        if (wasNameAssignment(command)) {
            return "";
        }
        return "error : " + command;
    }

    private static class Assignment {

    }
    String isAssignment(String command,String key) {
        String[] parts = command.split("=");
        if (parts.length==2 && is(key,parts[0])) {
            return parts[1].trim();
        }
        return null;
    }

    boolean wasNameAssignment(String command) {
        String value = isAssignment(command,name);
        if (value==null) {
            return false;
        }
        snap.setName(value);
        return true;
    }

    boolean wasMonitorAssignment(String command) {
        String value = isAssignment(command,monitor);
        if (value==null) {
            return false;
        }
        monitorTopic = new Topic.Spec(value);
        return true;
    }

    boolean is(String command, String input) {
        return command.equalsIgnoreCase(input.trim());
    }

    @Override
    public String more() {
        return moreLines.isEmpty() ? null : moreLines.removeFirst();
    }
}
