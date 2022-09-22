package com.curtcox.snap.model;

import java.util.HashSet;
import java.util.Set;

/**
 * For eliminating duplicate packets.
 * This filter is stateful and only passes a given packet once.
 */
public final class UniquePacketFilter implements Packet.Filter {

    private final Set<Packet> passed = new HashSet<>();

    @Override
    public boolean passes(Packet packet) {
        if (passed.contains(packet)) {
            return false;
        }
        passed.add(packet);
        return true;
    }
}
