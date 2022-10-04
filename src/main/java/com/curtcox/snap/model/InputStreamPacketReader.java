package com.curtcox.snap.model;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static com.curtcox.snap.util.Check.*;
import static com.curtcox.snap.model.Packet.*;

/**
 * A Packet.Reader that reads from an InputStream.
 */
public final class InputStreamPacketReader implements Reader {

    final InputStream input;
    final byte[] buffer = new byte[Packet.MAX_SIZE];

    public InputStreamPacketReader(InputStream input) {
        this.input = notNull(input);
    }

    public Packet read(Filter filter) throws IOException {
        if (input.available()<1) {
            return null;
        }
        int count = input.read(buffer);
        if (count<0) {
            return null;
        }
        byte[] raw = Arrays.copyOf(buffer,count);
        Bytes bytes = new Bytes(raw);
        return Packet.from(bytes);
    }

    public static List<Packet> readWaiting(InputStream in) throws IOException {
        List<Packet> packets = new ArrayList<>();
        InputStreamPacketReader reader = new InputStreamPacketReader(in);
        for (Packet packet = reader.read(ANY); packet!=null; packet=reader.read(ANY)) {
            packets.add(packet);
        }
        return packets;
    }
}
