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

    @Test
    public void equal() {
        assertEqual(new Topic("a"),new Topic("a"));
        assertEqual(new Topic("b"),new Topic("b"));
    }

    @Test
    public void does_not_match_when_different_literals() {
        assertNoMatch(new Topic("a"),new Topic("b"));
    }

    @Test
    public void matches_when_same_literal() {
        assertMatch(new Topic("a"),new Topic("a"));
        assertMatch(new Topic("b"),new Topic("b"));
    }

    @Test
    public void star_matches_everything() {
        assertMatch(new Topic("*"),new Topic("a"));
        assertMatch(new Topic("*"),new Topic("b"));
    }

    private void assertNotEqual(Topic a, Topic b) {
        assertNotEquals(a,b);
        assertNotEquals(b,a);
        assertNotEquals(a.hashCode(),b.hashCode());
    }

    private void assertEqual(Topic a, Topic b) {
        assertEquals(a,b);
        assertEquals(b,a);
        assertEquals(a.hashCode(),b.hashCode());
    }

    private void assertNoMatch(Topic a, Topic b) {
        assertFalse(a.matches(b));
        assertFalse(b.matches(a));
    }

    private void assertMatch(Topic a, Topic b) {
        assertTrue(a.matches(b));
        assertTrue(b.matches(a));
    }

}
