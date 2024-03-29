package com.curtcox.snap.model;

/**
 *
 * A list of packets that can be accessed by multiple threads concurrently.
 * This is to support different threads concurrently adding and removing packets.
 * A given packet may be seen multiple times via different mechanisms, but will no longer be returned once it is
 * either taken or removed via a Packet.Reader. In other words, a packet can only be consumed once.
 * Readers will return null when the element that would have been returned
 * is consumed via a different method (possibly on a different thread).
 */
final class PacketReaderFactory
        implements Packet.Reader.Factory, Packet.Sink
{

    private final ConcurrentPacketList list = new ConcurrentPacketList();

    // other
    @Override
    public Packet.Reader reader(Packet.Filter readerFilter) {
        return packetFilter -> list.isEmpty()
            ? null
            : list.read(packet -> readerFilter.passes(packet) && packetFilter.passes(packet));
    }

    // network
    Packet take() {
        return (list.isEmpty()) ? null : list.read(Packet.ANY);
    }

    // network or other
    @Override
    public boolean add(Packet packet) {
        return list.add(packet);
    }

}
