package com.curtcox;

import org.junit.Test;

import static org.junit.Assert.*;

public class ConcurrentPacketListTest {

    @Test
    public void can_create() {
        assertNotNull(new ConcurrentPacketList());
    }
}
