package com.curtcox.snap.shell;

import javax.swing.*;

/**
 * Skeletal implementation of something the user can interact with to interactively run commands.
 * Implementors must provide outputResult and invoke execute.
 */
abstract class CommandShell {

    final Timer timer = new Timer(100, e -> pollRunner());

    final CommandRunner runner;

    CommandShell(CommandRunner runner) {
        this.runner = runner;
    }

    abstract void outputResult(String result);

    final void pollRunner() {
        String output = runner.more();
        if (output!=null) {
            outputResult(output);
        }
    }

    /**
     * Implementors must invoke this when needed.
     */
    final String execute(String command) {
        return runner.execute(command);
    }

}
