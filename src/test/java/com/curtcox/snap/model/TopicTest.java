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
        assertNotEqual(new Topic.Spec("a"),new Topic("b"));
    }

    @Test
    public void equal() {
        assertEqual(new Topic.Spec("a"),new Topic("a"));
        assertEqual(new Topic.Spec("b"),new Topic("b"));
    }

    @Test
    public void does_not_match_when_different_literals() {
        assertNoMatch(new Topic.Spec("a"),new Topic("b"));
    }

    @Test
    public void matches_when_same_literal() {
        assertMatch(new Topic.Spec("a"),new Topic("a"));
        assertMatch(new Topic.Spec("b"),new Topic("b"));
    }

    @Test
    public void star_matches_everything() {
        assertMatch(new Topic.Spec("*"),new Topic("a"));
        assertMatch(new Topic.Spec("*"),new Topic("b"));
    }

    private void assertNotEqual(Topic.Spec a, Topic b) {
        assertNotEquals(a,b);
        assertNotEquals(b,a);
        assertNotEquals(a.hashCode(),b.hashCode());
    }

    private void assertEqual(Topic.Spec a, Topic b) {
        assertEquals(a,b);
        assertEquals(b,a);
        assertEquals(a.hashCode(),b.hashCode());
    }

    private void assertNoMatch(Topic.Spec a, Topic b) {
        assertFalse(a.matches(b));
        assertFalse(b.matches(a));
    }

    private void assertMatch(Topic.Spec a, Topic b) {
        assertTrue(a.matches(b));
        assertTrue(b.matches(a));
    }

}
