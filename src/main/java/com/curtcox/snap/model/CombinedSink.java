package com.curtcox.snap.model;

import com.curtcox.snap.model.Packet.*;

import java.util.*;

final class CombinedSink implements Sink {

    final List<Sink> sinks = new ArrayList<>();

    @Override
    public boolean add(Packet packet) {
        boolean accepted = false;
        for (Sink sink : sinks) {
            accepted = accepted || sink.add(packet);
        }
        return accepted;
    }

    void add(Sink sink) {
         sinks.add(sink);
    }
}
