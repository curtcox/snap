package com.curtcox;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static com.curtcox.Check.notNull;

/**
 * An immutable byte array.
 */
final class Bytes {

    private final byte[] bytes;

    final int length;

    Bytes(byte[] bytes) {
        this.bytes = notNull(bytes);
        this.length = bytes.length;
    }

    Bytes() {
        this(new byte[0]);
    }

    static Bytes from(String value) {
        return new Bytes(value.getBytes(StandardCharsets.UTF_8));
    }

    static Bytes bytes(int... values) {
        byte[] bytes = new byte[values.length];
        for (int i=0; i<values.length; i++) {
            bytes[i] = (byte) values[i];
        }
        return new Bytes(bytes);
    }

    static Bytes from(byte[]... arrays) {
        int size = 0;
        for (byte[] a : arrays) {
            size = size + a.length;
        }
        byte[] out = new byte[size];
        int start = 0;
        for (byte[] a : arrays) {
            for (int i=0; i<a.length; i++) {
                out[start + i] = a[i];
            }
            start = start + a.length;
        }
        return new Bytes(out);
    }

    byte at(int i) {
        return bytes[i];
    }

    boolean startsWith(Bytes prefix) {
        if (prefix.bytes.length > bytes.length) {
            return false;
        }
        for (int i=0; i< prefix.length; i++) {
            if (bytes[i]!=prefix.bytes[i]) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean equals(Object o) {
        Bytes that = (Bytes) o;
        return Arrays.equals(bytes,that.bytes);
    }

    @Override
    public int hashCode() {
        return bytes.length == 0 ? 0 : bytes[0] * bytes.length;
    }

    byte[] value() {
        return Arrays.copyOf(bytes,bytes.length);
    }
}
