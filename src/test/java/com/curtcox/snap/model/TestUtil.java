package com.curtcox.snap.model;

import java.io.IOException;

import static com.curtcox.snap.model.Clock.tick;
import static org.junit.Assert.assertEquals;

public final class TestUtil {

    static void assertEqualBytes(byte[] a, byte[] b) {
        assertEquals("Different lengths " + a.length + "!=" + b.length ,a.length,b.length);
        for (int i=0; i<a.length; i++) {
            assertEquals(a[i] + "!=" + b[i] + " at index " + i, a[i],b[i]);
        }
    }

    static void assertEqualBytes(Bytes a, Bytes b) {
        assertEquals("Different lengths " + a.length + "!=" + b.length ,a.length,b.length);
        for (int i=0; i<a.length; i++) {
            assertEquals(a.at(i) + "!=" + b.at(i) + " at index " + i, a.at(i),b.at(i));
        }
    }

    static void assertEqualBytes(Bytes a, byte[] b) {
        assertEqualBytes(a,new Bytes(b));
    }

    static void assertEqualBytes(byte[] a, Bytes b) {
        assertEqualBytes(new Bytes(a),b);
    }

    static void tick() {
        tick(1);
    }

    static void tick(int count) {
        try {
            Thread.sleep(tick *  count);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    static Packet consume(Node node) throws IOException {
        return node.read().read();
    }

    static Packet consume(Snap snap) throws IOException {
        return snap.listen().read();
    }

}
