package com.curtcox.snap.model;

import com.curtcox.snap.model.Packet.*;

import java.util.*;

/**
 * A single sink that accepts packets for potentially multiple other sinks.
 */
final class CombinedSink implements Sink {

    private final List<Sink> sinks = new ArrayList<>();

    @Override
    public boolean add(Packet packet) {
        boolean accepted = false;
        for (Sink sink : sinks) {
            accepted = sink.add(packet) || accepted;
        }
        return accepted;
    }

    /**
     * Add a sink.
     * Duplicate sinks are ignored rather than duplicating delivery or throwing an exception.
     * This prevents duplicate delivery and allows upstream use without tracking what sinks have been added.
     */
    void add(Sink sink) {
        if (!sinks.contains(sink)) {
            sinks.add(sink);
        }
    }
}
