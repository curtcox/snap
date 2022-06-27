package com.curtcox;

import java.util.*;

/**
 * A list of packets that can be accessed by multiple threads concurrently.
 * A given packet may be seen multiple times via different mechanisms, but will no longer be returned once it is
 * either taken or removed via iterator. In other words, a packet can only be consumed once.
 */
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
                    throw new NoSuchElementException();
                }
                lastRead = lastRead==null ? list.get(0) : after(lastRead);
                return lastRead;
            }

            @Override // TODO Test directly.
            public void remove() {
                list.remove(lastRead);
            }

        };
    }

    private boolean areMoreAfter(Packet lastRead) {
        for (Iterator<Packet> i=list.iterator();i.hasNext();) {
            Packet packet = i.next();
            if (lastRead.equals(packet)) {
                return i.hasNext();
            }
        }
        return false;
    }

    private Packet after(Packet lastRead) {
        for (Iterator<Packet> i=list.iterator();i.hasNext();) {
            Packet packet = i.next();
            if (lastRead.equals(packet)) {
                return i.next();
            }
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
