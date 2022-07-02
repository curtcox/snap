package com.curtcox.snap.util;

public final class Check {
    public static <T> T notNull(T t) {
        if (t==null) {
            throw new NullPointerException();
        }
        return t;
    }

}
