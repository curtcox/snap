package com.curtcox.snap.model;

import org.junit.*;

import java.io.*;
import java.util.*;

import static com.curtcox.snap.model.Packet.ANY;
import static org.junit.Assert.*;

public class CombinedReaderTest {

    @Test
    public void packet_is_null_when_there_are_no_readers() throws IOException {
        CombinedReader reader = new CombinedReader();
        assertNull(reader.read(ANY));
    }

    @Test
    public void reads_packets_from_one_reader() throws IOException {
        Packet packet = Random.packet();
        CombinedReader reader = new CombinedReader(reader(packet));
        assertEquals(packet,reader.read(ANY));
    }

    private Packet.Reader reader(Packet... packets) {
        final LinkedList<Packet> list = new LinkedList<>(Arrays.asList(packets));
        return filter -> list.isEmpty() ? null : list.removeFirst();
    }

}
