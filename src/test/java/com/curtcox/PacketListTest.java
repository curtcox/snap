package com.curtcox;

import org.junit.Test;

import java.util.Iterator;

import static org.junit.Assert.*;

public class PacketListTest {

    PacketList list = new PacketList();

    @Test
    public void can_create() {
        assertNotNull(new PacketList());
    }

    @Test
    public void read_works_as_expected_with_take_in_the_middle() {
        Iterator<Packet> read = list.read();
        list.take();
        fail();
    }

    @Test
    public void read_works_as_expected_with_add_in_the_middle() {
        Iterator<Packet> read = list.read();
        list.add(new Packet("",""));
        fail();
    }

    @Test
    public void read_works_as_expected_with_read_in_the_middle() {
        Iterator<Packet> read1 = list.read();
        Iterator<Packet> read2 = list.read();
        fail();
    }

}
