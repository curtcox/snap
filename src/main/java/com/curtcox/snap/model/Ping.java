package com.curtcox.snap.model;

import java.util.function.Predicate;

/**
 * For determining who can-hear / is-listening-to whom.
 */
public final class Ping {

    /** Don't make me. */
    private Ping() {}
    public static final String REQUEST = "ping request";
    public static final String RESPONSE = "ping response";

    public static Predicate<Packet> isRequest = packet -> packet.message.equals(REQUEST);
    public static Predicate<Packet> isResponse = packet -> packet.message.equals(RESPONSE);
}
