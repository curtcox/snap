package com.curtcox.snap.model;

import java.io.IOException;

import static org.junit.Assert.*;
import com.curtcox.snap.model.Packet.*;

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

    static Packet consume(Node node) throws IOException {
        return node.reader(Packet.ANY).read(Packet.ANY);
    }

    static Packet consume(Snap snap) throws IOException {
        return snap.reader().read(Packet.ANY);
    }

    static Packet consume(Snap snap,Topic topic) throws IOException {
        return Await.packet(snap.reader(topic));
    }
    
    static void assertContains(String whole, String part) {
        assertTrue(whole + " should contain " + part, whole.contains(part));
    }
}
