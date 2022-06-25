package com.curtcox;

import org.junit.Test;

import static org.junit.Assert.*;

// Direct tests for things in the Readme.
public class SnapReadmeTest {

    final SimpleNetwork network = SimpleNetwork.newPolling();

    final Snap snap1 = new Snap(Node.on(network));
    final Snap snap2 = new Snap(Node.on(network));

//    snap [ send | listen | listen-ping |listen-all | ping | whoami | interactive | awt | swing ]

    @Test
    public void whoami_returns_the_identity_with_machine_and_user_name() {
        String identity = snap1.whoami();
        assertTrue(identity.contains(snap1.user()));
        assertTrue(identity.contains(snap1.host()));
        assertEquals(snap1.user() + "@" + snap1.host(),identity);
    }

    @Test
    public void whoami_returns_the_name_given() {
        String expected = Random.random("name");
        snap1.setName(expected);
        String actual = snap1.whoami();
        assertEquals(expected,actual);
    }

    @Test
    public void ping_returns_whoami_names() {
        snap1.ping();
        snap1.listen();
    }
}
