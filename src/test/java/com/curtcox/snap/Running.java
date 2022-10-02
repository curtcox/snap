package com.curtcox.snap;

public final class Running {

    public static boolean inTheCloud() {
        return Boolean.parseBoolean(System.getenv("CI"));
    }
}
