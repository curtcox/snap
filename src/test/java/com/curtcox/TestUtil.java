package com.curtcox;

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

    static void shortDelay() {
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
