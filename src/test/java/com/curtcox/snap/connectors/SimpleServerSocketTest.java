package com.curtcox.snap.connectors;

import com.curtcox.snap.model.Runner;
import com.curtcox.snap.model.TestRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static com.curtcox.snap.model.TestClock.tick;
import static org.junit.Assert.*;

public class SimpleServerSocketTest {

    @Rule
    public Timeout globalTimeout = Timeout.seconds(2);

    Runner runner = Runner.of();

    @After
    public void stop() {
        runner.stop();
    }

    InputStream inputStream = new ByteArrayInputStream(new byte[0]);
    OutputStream outputStream = new ByteArrayOutputStream();

    ServerSocket serverSocket;
    Socket clientSocket = new Socket() {
        @Override public InputStream getInputStream() {
            return inputStream;
        }
        @Override public OutputStream getOutputStream() {
            return outputStream;
        }
    };

    List<StreamIO> streams = new ArrayList<>();
    Consumer<StreamIO> bridge = streamIO -> {
        streams.add(streamIO);
    };

    @Before
    public void setUp() throws IOException {
        serverSocket = new ServerSocket() {
            @Override public Socket accept() {
                return clientSocket;
            }
        };
    }

    @Test
    public void can_create_for_TCP() {
        assertNotNull(SimpleServerSocket.forTCP(null,null,runner));
    }

    @Test
    public void bridge_streams_starts_empty() {
        Runner runner = TestRunner.once();
        assertEquals(0,streams.size());
        SimpleServerSocket.forTCP(serverSocket,bridge,runner);
        assertEquals(1,streams.size());
    }

    @Test
    public void accept_returns_client_socket_with_righ_streams() throws IOException {
        TCP.ClientSocket client = tcpServer().accept();

        assertEquals(inputStream,client.asStreamIO().in);
        assertEquals(outputStream,client.asStreamIO().out);
    }

    @Test
    public void start_adds_client_socket_streams_to_bridge() {
        tcpServer().start(runner);
        tick(3);

        assertTrue(streams.size()>0);
        StreamIO streamIO = streams.get(0);
        assertEquals(inputStream,streamIO.in);
        assertEquals(outputStream,streamIO.out);
    }

    private SimpleServerSocket tcpServer() {
        return SimpleServerSocket.forTCP(serverSocket,bridge,runner);
    }
}
