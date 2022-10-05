package com.curtcox.snap.model;

import java.io.*;
import java.util.*;

import static com.curtcox.snap.util.Check.*;
import static com.curtcox.snap.model.Packet.*;

/**
 * A Packet.Reader that reads from an InputStream.
 */
public final class InputStreamPacketReader implements Packet.Reader {

    final BufferedInputStream input;
    final byte[] buffer = new byte[MAX_SIZE];

    public InputStreamPacketReader(InputStream input) {
        this.input = new BufferedInputStream(notNull(input));
    }

    public Packet read(Filter filter) throws IOException {
        input.mark(MAX_SIZE);
        if (input.available()<1) {
            return null;
        }
        int count = input.read(buffer);
        if (count<0) {
            return null;
        }
        byte[] raw = Arrays.copyOf(buffer,count);
        Bytes bytes = new Bytes(raw);
        Packet packet = Packet.from(bytes);
        input.reset();
        input.skip(packet.asBytes().length);
        return packet;
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
