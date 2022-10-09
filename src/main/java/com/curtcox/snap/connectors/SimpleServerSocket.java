package com.curtcox.snap.connectors;

import com.curtcox.snap.model.Debug;
import com.curtcox.snap.model.Runner;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.function.Consumer;

final class SimpleServerSocket {

    final ServerSocket socket;
    final TCP.ClientSocket.Factory factory;
    final Consumer<StreamIO> streams;

    static final boolean debug = Debug.on;

    SimpleServerSocket(ServerSocket socket, TCP.ClientSocket.Factory factory, Consumer<StreamIO> streams) {
        this.socket = socket;
        this.factory = factory;
        this.streams = streams;
    }

    static SimpleServerSocket forTCP(ServerSocket socket, Consumer<StreamIO> streams, Runner runner) {
        SimpleServerSocket server = new SimpleServerSocket(socket,accepted -> new SimpleClientSocket(accepted),streams);
        server.start(runner);
        return server;
    }

    TCP.ClientSocket accept() throws IOException {
        return factory.client(socket.accept());
    }

    void start(Runner runner) {
        if (debug) debug(this + " is listening to " + socket);
        runner.periodically(() -> {
            streams.accept(accept().asStreamIO());
            return null;
        });
    }

    private static void debug(String message) {
        System.out.println("SimpleServerSocket " + message);
    }

}