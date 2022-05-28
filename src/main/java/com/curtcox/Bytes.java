package com.curtcox;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import static com.curtcox.Check.notNull;

/**
 * An immutable byte array.
 */
public final class Bytes {

    private final byte[] bytes;

    public final int length;

    public Bytes(byte[] bytes) {
        this.bytes = notNull(bytes);
        this.length = bytes.length;
    }

    public Bytes() {
        this(new byte[0]);
    }

    public static Bytes from(String value) {
        try {
            return new Bytes(value.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static Bytes from(byte[]... arrays) {
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

    public byte at(int i) {
        return bytes[i];
    }

    public boolean startsWith(Bytes prefix) {
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

    public byte[] value() {
        return Arrays.copyOf(bytes,bytes.length);
    }
}
