package com.curtcox.snap.shell;

final class AwtShellDemo {

    public static void main(String args[]) {
        com.curtcox.snap.shell.AwtShell shell = new com.curtcox.snap.shell.AwtShell(new EchoCommandRunner());
        shell.init();
    }

}
