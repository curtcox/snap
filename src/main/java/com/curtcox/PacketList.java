package com.curtcox;

import java.util.*;

/**
 * A list of packets that can be accessed by multiple threads concurrently.
 * This is to support different threads concurrently adding and removing packets.
 * A given packet may be seen multiple times via different mechanisms, but will no longer be returned once it is
 * either taken or removed via a Packet.Reader. In other words, a packet can only be consumed once.
 * Readers will return null when the element that would have been returned
 * is consumed via a different method (possibly on a different thread).
 */
final class PacketList {

    private final ConcurrentPacketList list = new ConcurrentPacketList();

    // other
    Packet.Reader read() {
        return new Packet.Reader() {

            Packet lastRead = null;

//            @Override
//            public boolean hasNext() {
//                return lastRead==null ? !list.isEmpty() : list.areMoreAfter(lastRead);
//            }

            @Override
            public Packet read() {
                if (list.isEmpty()) {
                    return null;
                }
                lastRead = lastRead==null ? list.first() : list.after(lastRead);
                return lastRead;
            }

//            @Override
//            public void remove() {
//                Packet toRemove = lastRead;
//                lastRead = list.before(toRemove);
//                list.remove(toRemove);
//            }

        };
    }


    // other
    Packet.Reader read(String topic) {
        return new PacketIteratorFilter(read(),new TopicPacketFilter(topic));
    }

    // network
    Packet take() {
        return (list.isEmpty()) ? null : list.removeFirst();
    }

    // network or other
    void add(Packet packet) {
        list.add(packet);
    }

}
