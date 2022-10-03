package com.curtcox.snap.connectors;

import com.curtcox.snap.model.Packet;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public final class BufferStreamIO implements Packet.IO {

    final LinkedList<Packet> toRead;
    final List<Packet> written;

    BufferStreamIO(LinkedList<Packet> toRead, List<Packet> written) {
        this.toRead = toRead;
        this.written = written;
    }

    static BufferStreamIO with(Packet... packets) {
        return new BufferStreamIO(new LinkedList<>(Arrays.asList(packets)),new ArrayList<>());
    }

    @Override
    public Packet read(Packet.Filter filter) throws IOException {
        return toRead.isEmpty() ? null : toRead.removeFirst();
    }

    @Override
    public void write(Packet packet) throws IOException {
        written.add(packet);
    }

    StreamIO asStreamIO() {
        return null;
    }
}
