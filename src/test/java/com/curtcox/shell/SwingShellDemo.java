package com.curtcox.shell;

final class SwingShellDemo {

    public static void main(String args[]) {
        SwingShell shell = new SwingShell(new EchoCommandRunner());
        shell.init();
    }

}
