package it.polimi.ingsw.view;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProxyView {
    private final String ip;
    private final int port;
    ExecutorService executor = Executors.newFixedThreadPool(4);

    public ProxyView(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }


    public void start() {
        // Set up Tcp socket and spawn threads for connections

        // create socket and keep accepting connections

        // On successful connection spawn ClientHandler
        // eg. proxyView.executor.submit(new ClientHandler(socket));
    }
}
