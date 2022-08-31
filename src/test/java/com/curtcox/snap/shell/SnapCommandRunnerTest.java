package com.curtcox.snap.shell;

import com.curtcox.snap.model.*;
import org.junit.Test;

import static org.junit.Assert.*;

public class SnapCommandRunnerTest {

    Snap snap = Snap.newInstance();
    SnapCommandRunner runner = new SnapCommandRunner(snap);

    @Test
    public void can_create() {
        assertNotNull(new SnapCommandRunner(Snap.newInstance()));
    }

    @Test
    public void unknown_command_returns_null() {
        assertEquals("error : what's all this then",runner.execute("what's all this then"));
    }

    @Test
    public void whoami() {
        assertEquals(snap.whoami(),runner.execute("whoami"));
    }

    @Test
    public void whoami_ignores_whitespace() {
        assertEquals(snap.whoami(),runner.execute("    whoami    "));
    }

    @Test
    public void whoami_returns_name_set() {
        String name = Random.random("name");
        assertEquals("",runner.execute("name="+name));
        assertEquals(name,runner.execute("whoami"));
    }

    @Test
    public void whoami_returns_trimmed_name_set() {
        String name = Random.random("name");
        assertEquals("",runner.execute("  name  =  " + name + "   "));
        assertEquals(name,runner.execute("whoami"));
    }

}
