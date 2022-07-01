package com.curtcox;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

final class ConcurrentPacketList {
    private final List<Packet> list = new ArrayList<>();

//    boolean areMoreAfter(Packet packet) {
//        for (Iterator<Packet> i = list.iterator(); i.hasNext();) {
//            if (packet.equals(i.next())) {
//                return i.hasNext();
//            }
//        }
//        return false;
//    }

//    Packet after(Packet packet) {
//        for (Iterator<Packet> i=list.iterator();i.hasNext();) {
//            if (packet.equals(i.next())) {
//                return i.next();
//            }
//        }
//        return null;
//    }

//    Packet before(Packet target) {
//        Packet candidate = null;
//        for (Iterator<Packet> i=list.iterator();i.hasNext();) {
//            Packet packet = i.next();
//            if (target.equals(packet)) {
//                return candidate;
//            }
//            candidate = packet;
//        }
//        return null;
//    }

    boolean isEmpty() {
        return list.isEmpty();
    }

    void add(Packet packet) {
        list.add(packet);
    }

//    Packet first() {
//        return list.get(0);
//    }

    Packet removeFirst() {
        return list.remove(0);
    }

    Packet removeFirstMatching(Packet.Filter filter) {
        for (Packet packet : list) {
            if (filter.passes(packet)) {
                list.remove(packet);
                return packet;
            }
        }
        return null;
    }

//    void remove(Packet toRemove) {
//        list.remove(toRemove);
//    }
}
