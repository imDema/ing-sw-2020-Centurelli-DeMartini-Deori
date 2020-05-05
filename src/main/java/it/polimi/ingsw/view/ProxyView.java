package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.GameController;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProxyView {
    private final String ip;
    private final int port;
    private GameController controller = new GameController();
    ExecutorService executor = Executors.newFixedThreadPool(4);

    public ProxyView(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public void start() throws IOException {
        // Set up Tcp socket and spawn threads for connections
        ServerSocket server;
        try {
            server = new ServerSocket(port);
        } catch (IOException e){
            System.err.println("Port " + port + " is already in use");
            return;
        }
        // create socket and keep accepting connections
        // On successful connection spawn ClientHandler
        // eg. proxyView.executor.submit(new ClientHandler(socket));
        int connections = 0;
        while (true) {
            try {
                Socket s = server.accept();
                executor.submit(new ClientHandler(s.getInputStream(), s.getOutputStream(), controller));
                connections += 1;
                if(connections == 3)
                    break;
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
