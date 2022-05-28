package com.curtcox;

final class Random {

    static String random(String prefix) {
        return prefix + System.currentTimeMillis() % 1000;
    }

}
