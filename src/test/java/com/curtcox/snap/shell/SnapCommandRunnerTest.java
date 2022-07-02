package com.curtcox.snap.shell;

import com.curtcox.snap.model.Snap;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class SnapCommandRunnerTest {

    @Test
    public void can_create() {
        assertNotNull(new com.curtcox.snap.shell.SnapCommandRunner(Snap.newInstance()));
    }
}
