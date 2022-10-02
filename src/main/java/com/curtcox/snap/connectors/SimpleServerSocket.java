package com.curtcox.snap.connectors;

import com.curtcox.snap.model.Runner;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.function.Consumer;

final class SimpleServerSocket {

    final ServerSocket socket;
    final TCP.ClientSocket.Factory factory;
    final Consumer<StreamIO> streams;

    SimpleServerSocket(ServerSocket socket, TCP.ClientSocket.Factory factory, Consumer<StreamIO> streams) {
        this.socket = socket;
        this.factory = factory;
        this.streams = streams;
    }

    static SimpleServerSocket forTCP(ServerSocket socket, Consumer<StreamIO> streams) {
        return new SimpleServerSocket(socket,accepted -> new SimpleClientSocket(accepted),streams);
    }

    TCP.ClientSocket accept() throws IOException {
        return factory.client(socket.accept());
    }

    void start(Runner runner) {
        System.out.println("App is listening to " + socket);
        runner.periodically(() -> {
            streams.accept(accept().asStreamIO());
            return null;
        });
    }


}