package com.curtcox;

import java.util.*;

import static com.curtcox.Check.notNull;

final class PacketIteratorFilter implements Iterator<Packet> {

    private final Iterator<Packet> inner;
    private final Packet.Filter filter;

    private Packet next;

    public PacketIteratorFilter(Iterator<Packet> inner,Packet.Filter filter) {
        this.inner = notNull(inner);
        this.filter = notNull(filter);
    }

    @Override
    public boolean hasNext() {
        return next != null ? true : findNext() != null;
    }

    private Packet findNext() {
        while (next==null && inner.hasNext()) {
            Packet candidate = inner.next();
            if (filter.passes(candidate)) {
                next = candidate;
                return next;
            }
        }
        return null;
    }

    @Override
    public Packet next() {
        if (next == null) {
            findNext();
        }
        Packet prior = next;
        next = null;
        return prior;
    }

    @Override
    public void remove() {
        inner.remove();
    }

}
