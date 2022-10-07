package com.curtcox.snap.connectors;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

final class FakeServerSocket extends ServerSocket {

    final StreamIO io;
    public FakeServerSocket(StreamIO io) throws IOException {
        this.io = io;
    }

    @Override
    public Socket accept() {
        return new Socket() {
            @Override public InputStream getInputStream()   { return io.in;  }
            @Override public OutputStream getOutputStream() { return io.out; }
        };
    }
}
