package com.curtcox.snap.model;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.io.*;

import static com.curtcox.snap.model.Packet.ANY;
import static org.junit.Assert.*;

public class PipeTest {

    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    @Test
    public void can_create() throws IOException {
        assertNotNull(new Pipe());
    }

    @Test
    public void can_send_packet_from_left_to_right() throws IOException {
        Pipe pipe = new Pipe();
        Packet packet = Random.packet();
        PacketReaderWriter writer = PacketReaderWriter.from(pipe.left);
        PacketReaderWriter reader = PacketReaderWriter.from(pipe.right);
        writer.write(packet);

        Packet read = reader.read(ANY);
        assertEquals(packet,read);
    }

    @Test
    public void can_send_packet_from_right_to_left() throws IOException {
        Pipe pipe = new Pipe();
        Packet packet = Random.packet();
        PacketReaderWriter writer = PacketReaderWriter.from(pipe.right);
        PacketReaderWriter reader = PacketReaderWriter.from(pipe.left);
        writer.write(packet);

        Packet read = reader.read(ANY);
        assertEquals(packet,read);
    }

}
