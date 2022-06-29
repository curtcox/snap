package com.curtcox;

import java.util.*;

/**
 * A list of packets that can be accessed by multiple threads concurrently.
 * This is to support different threads concurrently adding and removing packets.
 * A given packet may be seen multiple times via different mechanisms, but will no longer be returned once it is
 * either taken or removed via iterator. In other words, a packet can only be consumed once.
 * Iterators will return null instead of throwing NoSuchElementException when the element that would have been returned
 * is consumed via a different method (possibly on a different thread).
 */
// TODO Why does PacketList need to support concurrent modification?
// TODO Add thread safety tests.
final class PacketList {

    private final List<Packet> list = new ArrayList<>();

    Iterator<Packet> read() {
        return new Iterator<Packet>() {

            Packet lastRead = null;

            @Override
            public boolean hasNext() {
                return lastRead==null ? !list.isEmpty() : areMoreAfter(lastRead);
            }

            @Override
            public Packet next() {
                if (list.isEmpty()) {
                    return null;
                }
                lastRead = lastRead==null ? list.get(0) : after(lastRead);
                return lastRead;
            }

            @Override
            public void remove() {
                Packet toRemove = lastRead;
                lastRead = before(toRemove);
                list.remove(toRemove);
            }

        };
    }

    private boolean areMoreAfter(Packet packet) {
        for (Iterator<Packet> i=list.iterator();i.hasNext();) {
            if (packet.equals(i.next())) {
                return i.hasNext();
            }
        }
        return false;
    }

    private Packet after(Packet packet) {
        for (Iterator<Packet> i=list.iterator();i.hasNext();) {
            if (packet.equals(i.next())) {
                return i.next();
            }
        }
        return null;
    }

    private Packet before(Packet target) {
        Packet candidate = null;
        for (Iterator<Packet> i=list.iterator();i.hasNext();) {
            Packet packet = i.next();
            if (target.equals(packet)) {
                return candidate;
            }
            candidate = packet;
        }
        return null;
    }

    Packet read(String topic) {
        for (Iterator<Packet> i=read();i.hasNext();) {
            Packet packet = i.next();
            if (topic.equals(packet.topic)) {
                i.remove();
                return packet;
            }
        }
        return null;
    }

    Packet take() {
        return (list.isEmpty()) ? null : list.remove(0);
    }

    void add(Packet packet) {
        list.add(packet);
    }

}
