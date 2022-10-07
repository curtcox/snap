package com.curtcox.snap.model;

import com.curtcox.snap.connectors.StreamIO;

import java.io.*;

/**
 * For moving bytes between two places -- both ways.
 */
public final class Pipe {

    private final Half top = new Half();
    private final Half bottom = new Half();
    public final StreamIO left = new StreamIO(top.in,bottom.out);
    public final StreamIO right = new StreamIO(bottom.in,top.out);

    /*
     * For moving bytes between two places, but only in one way.
     */
    static class Half {
        final PipedOutputStream out = new PipedOutputStream();
        final PipedInputStream in = new PipedInputStream(out,Packet.MAX_SIZE);

        Half() throws IOException {}
    }

    public Pipe() throws IOException {}

}
