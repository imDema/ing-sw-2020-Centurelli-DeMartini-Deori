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
        while (true) {
            try {
                Socket s = server.accept();
                executor.submit(new ClientHandler(s, controller));

                if(controller.isGameReady()){
                    System.out.println("Lobby if full");
                    s.close();
                }
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
