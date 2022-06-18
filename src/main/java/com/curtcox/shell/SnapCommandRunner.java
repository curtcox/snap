package com.curtcox.shell;

import com.curtcox.Snap;

final class SnapCommandRunner implements CommandRunner {

    final Snap snap;

    SnapCommandRunner(Snap snap) {
        this.snap = snap;
    }

    @Override
    public String execute(String command) {
        return null;
    }

    @Override
    public String more() {
        return null;
    }
}
