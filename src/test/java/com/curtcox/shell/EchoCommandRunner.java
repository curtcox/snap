package com.curtcox.shell;

final class EchoCommandRunner implements CommandRunner {

    @Override
    public String execute(String command) {
        return "Did " + command;
    }

}
