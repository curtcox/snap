package com.curtcox.snap.model;

import java.io.*;
import java.util.*;

/**
 * For treating multiple readers as a single reader AKA a Y.
 * See also SplitReader.
 */
final class CombinedReader implements Packet.Reader {

    private final List<Packet.Reader> readers;

    CombinedReader(List<Packet.Reader> readers) {
        this.readers = readers;
    }

    CombinedReader(Packet.Reader... readers) {
        this(Arrays.asList(readers));
    }

    @Override
    public Packet read(Packet.Filter filter) throws IOException {
        for (Packet.Reader reader : readers) {
            Packet packet = reader.read(filter);
            if (packet != null) {
                return packet;
            }
        }
        return null;
    }
}
