package com.curtcox.snap.model;

import java.io.*;

/**
 * So multiple readers can read from a single reader. AKA a T.
 * See also CombinedReader.
 */
final class SplitReader {

    private final Packet.Reader reader;

    SplitReader(Packet.Reader reader) {
        this.reader = reader;
    }

    Packet.Reader reader(Packet.Filter filter) throws IOException {
        return reader;
    }
}
