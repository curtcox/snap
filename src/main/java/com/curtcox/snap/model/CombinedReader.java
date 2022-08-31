package com.curtcox.snap.model;

import java.io.IOException;
import java.util.*;
import static com.curtcox.snap.model.Packet.*;
/**
 * For treating multiple readers as a single reader AKA a Y.
 * See also SplitReader.
 */
final class CombinedReader implements Reader {

    private final List<Reader> readers;

    CombinedReader(List<Reader> readers) {
        this.readers = readers;
    }

    CombinedReader(Reader... readers) {
        this(Arrays.asList(readers));
    }

    @Override
    public Packet read(Filter filter) throws IOException {
        for (Reader reader : readers) {
            Packet packet = reader.read(filter);
            if (packet != null) {
                return packet;
            }
        }
        return null;
    }
}
