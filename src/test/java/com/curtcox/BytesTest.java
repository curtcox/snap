package com.curtcox;

import org.junit.Test;

import static com.curtcox.TestUtil.assertEqualBytes;
import static org.junit.Assert.*;

public class BytesTest {

    @Test
    public void from_combines_given_arrays() {
        from(bytes(0),bytes(1),bytes(0,1));
        from(bytes(1),bytes(0),bytes(1,0));
        from(bytes(1,2,3),bytes(4,5),bytes(1,2,3,4,5));
    }

    private void from(Bytes a, Bytes b, Bytes c) {
        assertEqualBytes(c,Bytes.from(a.value(),b.value()));
    }

    @Test
    public void startsWith_true_when_bytes_starts_with_given_bytes() {
        assertTrue(bytes(0,1,2,3,4).startsWith(bytes(0,1,2)));
        assertTrue(bytes(3,1,4).startsWith(bytes(3,1)));
    }

    @Test
    public void startsWith_false_when_bytes_do_not_start_with_given_bytes() {
        assertFalse(bytes(0,1,2,3,4).startsWith(bytes(0,1,9)));
        assertFalse(bytes(3,1,4).startsWith(bytes(3,1,1)));
    }

    private Bytes bytes(int... values) {
        byte[] bytes = new byte[values.length];
        for (int i=0; i<values.length; i++) {
            bytes[i] = (byte) values[i];
        }
        return new Bytes(bytes);
    }

    @Test
    public void equal_bytes_are_equal() {
        assertBytesEqual(bytes(),bytes());
        assertBytesEqual(bytes(1),bytes(1));
        assertBytesEqual(bytes(2),bytes(2));
        assertBytesEqual(bytes(1,2),bytes(1,2));
    }

    private void assertBytesEqual(Bytes a, Bytes b) {
        assertEquals(a,b);
        assertEquals(b,a);
        assertEquals(b.hashCode(),a.hashCode());
    }

    @Test
    public void unequal_bytes_are_unequal() {
        assertNotEquals(bytes(),bytes(0));
        assertNotEquals(bytes(1),bytes(2));
        assertNotEquals(bytes(2),bytes(1));
        assertNotEquals(bytes(1,1),bytes(1,2));
    }

    @Test
    public void value_returns_equivalent_value() {
        value_returns_equivalent_value(bytes(0));
        value_returns_equivalent_value(bytes(3,1,4,1,5,9));
    }

    private void value_returns_equivalent_value(Bytes bytes) {
        assertBytesEqual(bytes,new Bytes(bytes.value()));
    }

}
