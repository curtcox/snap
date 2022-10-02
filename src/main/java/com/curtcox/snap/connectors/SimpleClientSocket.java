package com.curtcox.snap.connectors;

import java.io.IOException;
import java.net.Socket;

final class SimpleClientSocket implements TCP.ClientSocket {
    private Socket socket;

    SimpleClientSocket(Socket socket) {
        this.socket = socket;
    }

    @Override
    public StreamIO asStreamIO() throws IOException {
        return new StreamIO(socket.getInputStream(),socket.getOutputStream());
    }
}
