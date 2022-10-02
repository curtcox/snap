package com.curtcox.snap.connectors;

import java.io.InputStream;
import java.io.OutputStream;

public final class StreamIO {

    public final InputStream in;
    public final OutputStream out;

    public StreamIO(InputStream in, OutputStream out) {
        this.in = in;
        this.out = out;
    }

}
