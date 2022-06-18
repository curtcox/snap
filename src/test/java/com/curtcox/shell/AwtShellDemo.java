package com.curtcox.shell;

final class AwtShellDemo {

    public static void main(String args[]) {
        AwtShell shell = new AwtShell(new EchoCommandRunner());
        shell.init();
    }

}
