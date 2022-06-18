package com.curtcox.shell;

/**
 * Something that accepts commands from the user and runs them.
 */
interface CommandRunner {
    /**
     * Execute the given command and return the immediate output.
     */
    String execute(String command);

    /**
     * Return additional output or null if there is no additional output.
     */
    String more();
}
