package com.curtcox.snap.model;

import org.junit.*;
import com.curtcox.snap.model.Packet.*;

import static org.junit.Assert.*;

public class TopicTest {

    @Test
    public void can_create() {
        assertNotNull(new Topic(""));
    }

    @Test
    public void not_equal() {
        assertNotEqual(new Topic("a"),new Topic("b"));
    }

    private void assertNotEqual(Topic a, Topic b) {
        assertNotEquals(a,b);
        assertNotEquals(a.hashCode(),b.hashCode());
    }

}
