package com.curtcox.snap.model;

import com.curtcox.snap.model.Packet.*;

import java.util.*;

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

    void add(Sink sink) {
        if (sinks.contains(sink)) {
            String message = "This sink has already been added. Adding it again would produce duplicate deliveries.";
            throw new IllegalArgumentException(message);
        }
        sinks.add(sink);
    }
}
