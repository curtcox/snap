package com.curtcox.snap.connectors;

import com.curtcox.snap.model.Packet.*;
import com.curtcox.snap.model.PacketReaderWriter;
import com.curtcox.snap.model.SimpleNetwork;

/**
 * For reading from standard input and writing to standard output.
 */
public final class StandardIO {

    public static void main(String[] args) {
        on(network());
    }

    static void on(Network network) {
        network.add(io());
    }

    static Network network() {
        return SimpleNetwork.newPolling();
    }

    static PacketReaderWriter io() {
        return PacketReaderWriter.from(System.in,System.out);
    }

}
