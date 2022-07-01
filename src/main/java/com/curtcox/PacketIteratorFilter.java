package com.curtcox;

import java.io.IOException;
import java.util.*;

import static com.curtcox.Check.notNull;

final class PacketIteratorFilter implements Packet.Reader {

    private final Packet.Reader inner;
    private final Packet.Filter filter;

    private Packet next;

    public PacketIteratorFilter(Packet.Reader inner,Packet.Filter filter) {
        this.inner = notNull(inner);
        this.filter = notNull(filter);
    }

//    @Override
//    public boolean hasNext() {
//        return next != null ? true : findNext() != null;
//    }

    private Packet findNext() {
        while (next==null) {
            Packet candidate = readPacket();
            if (filter.passes(candidate)) {
                next = candidate;
                return next;
            }
        }
        return null;
    }

    private Packet readPacket() {
        try {
            return inner.read();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Packet read() {
        if (next == null) {
            findNext();
        }
        Packet prior = next;
        next = null;
        return prior;
    }

//    @Override
//    public void remove() {
//        inner.remove();
//    }

}
