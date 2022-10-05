package com.curtcox.snap.model;

import com.curtcox.snap.model.Packet.Topic;
import org.junit.Test;

import static org.junit.Assert.*;

public class TopicSpecTest {

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
    public void Duplicate_Sender_Notification() {
        assertMatch(new Topic.Spec(Topic.Duplicate_Sender_Notification),Topic.Duplicate_Sender_Notification);
    }

    @Test
    public void matches_when_tag_specified_is_one_of_the_tags_provided() {
        assertMatch(new Topic.Spec("a"),new Topic("a b c"));
        assertMatch(new Topic.Spec("b"),new Topic("a b c"));
    }

    @Test
    public void with_alone_matches_everything() {
        assertMatch(new Topic.Spec(""),new Topic("a"));
        assertMatch(new Topic.Spec(""),new Topic("b"));
        assertMatch(new Topic.Spec(""),new Topic("a b c"));
    }

    @Test
    public void without_alone_matches_nothing() {
        assertNoMatch(new Topic.Spec("!"),new Topic("a"));
        assertNoMatch(new Topic.Spec("!"),new Topic("b"));
        assertNoMatch(new Topic.Spec("!"),new Topic("a b c"));
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
