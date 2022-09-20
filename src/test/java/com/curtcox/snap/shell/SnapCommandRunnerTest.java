package com.curtcox.snap.shell;

import com.curtcox.snap.model.*;
import org.junit.Test;
import static com.curtcox.snap.model.TestClock.tick;

import static org.junit.Assert.*;

public class SnapCommandRunnerTest {

    SimpleNetwork network = SimpleNetwork.newPolling();
    Snap snap = Snap.on(network);
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
    public void monitor_returns_monitor_set() {
        String name = Random.random("name");
        assertEquals("",runner.execute("monitor="+name));
        assertEquals(name,runner.execute("monitor"));
    }

    @Test
    public void monitor_returns_monitor_set_when_set_to_empty() {
        assertEquals("",runner.execute("monitor="));
        assertEquals("",runner.execute("monitor"));
    }

    @Test
    public void monitor_returns_monitor_set_when_set_to_not() {
        assertEquals("",runner.execute("monitor=!"));
        assertEquals("!",runner.execute("monitor"));
    }

    @Test
    public void whoami_returns_trimmed_name_set() {
        String name = Random.random("name");
        assertEquals("",runner.execute("  name  =  " + name + "   "));
        assertEquals(name,runner.execute("whoami"));
    }

    @Test
    public void ping_returns_responses() {
        Snap.on(network);
        runner.more();
        assertEquals("",runner.execute("ping"));
        tick(2);

        assertContains(runner.more(),"ping response");
    }

    private void assertContains(String text, String part) {
        assertTrue(text + " should contain " + part,text.contains(part));
    }

    @Test
    public void initially_shows_help_text_once() {
        String text = runner.more();
        assertContains(text,"To see this text type help");
        assertContains(text,"whoami");
        assertContains(text,"name");
        assertNull(runner.more());
    }

    @Test
    public void shows_help_text_once_after_typing_help() {
        assertContains(runner.more(),"To see this text type help");
        String text = runner.execute("help");
        assertContains(text,"To see this text type help");
        assertNull(runner.more());
    }

}
