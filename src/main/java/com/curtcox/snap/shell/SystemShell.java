package com.curtcox.snap.shell;

import com.curtcox.snap.model.Packet.*;
import com.curtcox.snap.model.Snap;

import java.io.*;
import java.util.Scanner;

/**
 * For running commands from a terminal.
 */
public final class SystemShell extends CommandShell {

    private final InputStream in;
    private final PrintStream out;

    private SystemShell(InputStream in,PrintStream out,CommandRunner runner) {
        super(runner);
        this.in = in;
        this.out = out;
    }

    SystemShell(Snap snap) {
        this(System.in,System.out,runner(snap));
    }

    public static void start(Snap snap) {
        new Thread(() -> new SystemShell(snap).executeCommandsFromInput()).start();
    }

    public static void main(String[] args) {
        start(Snap.newInstance());
    }

    static CommandRunner runner(Snap snap) {
        return new SnapCommandRunner(snap);
    }

    public static void on(Network network) {
        start(Snap.on(network));
    }

    @Override void outputResult(String result) {
        out.println(result);
    }

    private void executeCommandsFromInput() {
        Scanner input = new Scanner(in);
        while (true) {
            outputResult(execute(input.nextLine()));
        }
    }

}
