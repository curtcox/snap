package com.curtcox.snap.connectors;

import com.curtcox.snap.model.Bytes;
import com.curtcox.snap.model.Debug;

import java.io.*;
import java.util.*;

/**
 * For debugging.
 */
final class DebugByteArrayOutputStream extends ByteArrayOutputStream {

    private boolean closed;
    final List<Write> writes = new ArrayList<>();

    static final class Write {
        final Bytes bytes;
        final Throwable at;

        Write(Bytes bytes) {
            this.bytes = bytes;
            at = new Throwable("Wrote " + bytes.toString());
            if (Debug.on) at.printStackTrace();
        }

        @Override
        public String toString() {
            return string(at);
        }
    }

    static String string(Throwable t) {
        StringWriter writer = new StringWriter();
        PrintWriter printer = new PrintWriter(writer);
        t.printStackTrace(printer);
        return writer.toString();
    }

    public synchronized void write(int b) {
        ensureOpen();
        super.write(b);
        writes.add(new Write(Bytes.bytes(b)));
    }

    public synchronized void write(byte[] b, int off, int len) {
        ensureOpen();
        super.write(b,off,len);
        writes.add(new Write(Bytes.bytes(b)));
    }

    public synchronized void reset() {
        throw new UnsupportedOperationException("Use a different stream");
    }

    public synchronized void close() {
        closed = true;
    }

    private void ensureOpen() {
        if (closed) {
            throw new IllegalStateException("Output stream is closed");
        }
    }

    @Override
    public String toString() {
        return writes.toString();
    }
}
