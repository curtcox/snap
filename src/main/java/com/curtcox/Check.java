package com.curtcox;

final class Check {
    static <T> T notNull(T t) {
        if (t==null) {
            throw new NullPointerException();
        }
        return t;
    }

}
