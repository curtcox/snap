package com.curtcox.snap.model;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import static com.curtcox.snap.model.Packet.ANY;

import static org.junit.Assert.*;

public class PacketRelayTest {

    ByteArrayInputStream inputStream;
    ByteArrayOutputStream outputStream;
    PacketReaderWriter relay;

    @Test(expected = NullPointerException.class)
    public void requires_inputs() {
        assertNotNull(new PacketReaderWriter(null,null));
    }

    private PacketReaderWriter createRelay(byte[] bytes) {
        inputStream = new ByteArrayInputStream(bytes);
        outputStream = new ByteArrayOutputStream();
        relay = new PacketReaderWriter(new InputStreamPacketReader(inputStream),new OutputStreamPacketWriter(outputStream));
        return relay;
    }

    @Test
    public void can_create() {
        assertNotNull(createRelay(new byte[0]));
    }

    @Test
    public void read_reads_packet_from_input_when_there_is_one() throws IOException {
        Packet packet = Random.packet();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputStreamPacketWriter writer = new OutputStreamPacketWriter(outputStream);
        writer.write(packet);
        createRelay(outputStream.toByteArray());

        Packet read = relay.read(ANY);

        assertEquals(packet,read);
    }

    @Test
    public void read_returns_null_when_there_is_no_input() throws IOException {
        createRelay(new byte[0]);

        Packet read = relay.read(ANY);

        assertNull(read);
    }

    @Test
    public void write_writes_packet_to_output() throws IOException {
        createRelay(new byte[0]);
        Packet packet = Random.packet();

        relay.write(packet);
        InputStreamPacketReader reader = new InputStreamPacketReader(new ByteArrayInputStream(outputStream.toByteArray()));
        Packet read = reader.read(ANY);

        assertEquals(packet,read);
    }

    @Test(expected = NullPointerException.class)
    public void write_throws_exception_when_packet_is_null() throws IOException {
        relay.write(null);
    }

}
