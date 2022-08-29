package com.curtcox.snap.shell;

import com.curtcox.snap.model.*;

final class SwingShellDemo {

    public static void main(String args[]) {
        snapShell();
    }

    static void echoShell() {
        new SwingShell(new EchoCommandRunner()).init();
    }

    static void snapShell() {
        new SwingShell(new SnapCommandRunner(Snap.newInstance())).init();
    }

}
