package com.curtcox.snap.connectors;

import java.net.*;

/**
 * Handy methods for dealing with sockets.
 */
final class Sockets {

    static InetSocketAddress socketAddress(int a1, int a2, int a3, int a4, int port) throws UnknownHostException {
        return new InetSocketAddress(address(a1,a2,a3,a4), port);
    }

    static InetAddress address(int a1, int a2, int a3, int a4) throws UnknownHostException {
        return InetAddress.getByAddress(new byte[]{b(a1),b(a2),b(a3),b(a4)});
    }

    private static byte b(int value) {
        if (value>255 || value<0) {
            throw new IllegalArgumentException(value + " is not between 0 and 255");
        }
        return (byte) value;
    }

}
