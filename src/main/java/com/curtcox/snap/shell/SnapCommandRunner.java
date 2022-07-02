package com.curtcox.snap.shell;

import com.curtcox.snap.model.Snap;

final class SnapCommandRunner implements com.curtcox.snap.shell.CommandRunner {

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
