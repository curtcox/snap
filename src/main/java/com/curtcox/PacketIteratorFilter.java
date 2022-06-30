package com.curtcox;

import java.util.*;

import static com.curtcox.Check.notNull;

final class PacketIteratorFilter implements Iterator<Packet> {

    private final Iterator<Packet> inner;
    private final Packet.Filter filter;

    public PacketIteratorFilter(Iterator<Packet> inner,Packet.Filter filter) {
        this.inner = notNull(inner);
        this.filter = notNull(filter);
    }

    @Override
    public boolean hasNext() {
        return false;
    }

    @Override
    public Packet next() {
        return null;
    }

    @Override
    public void remove() {
        inner.remove();
    }

}
