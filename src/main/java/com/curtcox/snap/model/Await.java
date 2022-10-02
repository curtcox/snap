package com.curtcox.snap.model;

import java.io.IOException;

/**
 * For concisely waiting until something happens.
 */
public final class Await {

    public static Packet packet(Packet.Reader reader) throws IOException {
        return packet(reader,10,Clock.standard);
    }

    public static Packet packet(Packet.Reader reader, int ticks, Clock clock) throws IOException {
        for (int i=0; i<ticks; i++) {
            Packet packet = reader.read(Packet.ANY);
            if (packet==null) {
                clock.tick();
            } else {
                return packet;
            }
        }
        return null;
    }
}
