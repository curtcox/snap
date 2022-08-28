package com.curtcox.snap.model;

import java.util.LinkedList;
import java.util.List;

/**
 * An ordered sequence of packets.
 * For easily creating multiple readers of the same packets.
 */
final class PacketStream
    implements Packet.Sink
{

    private long at;
    private final List<PacketAndPosition> packets = new LinkedList<>();
    PacketAndPosition after(Position position, Packet.Filter filter) {
        for (PacketAndPosition packetAndPosition : packets) {
            if ((position==null || packetAndPosition.position.after(position)) &&
                    filter.passes(packetAndPosition.packet)) {
                return packetAndPosition;
            }
        }
        return null;
    }

    @Override
    public boolean add(Packet packet) {
        at = at + 1;
        return packets.add(new PacketAndPosition(packet,new Position(at)));
    }

    static final class Position {
        final long value;

        Position(long value) {
            this.value = value;
        }

        boolean after(Position position) {
            return value > position.value;
        }
    }

    static final class PacketAndPosition {

        final Packet packet;
        final Position position;

        PacketAndPosition(Packet packet, Position position) {
            this.packet = packet;
            this.position = position;
        }

    }
}
