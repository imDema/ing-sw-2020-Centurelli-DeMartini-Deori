package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.GameController;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
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

    public void start() {
        // Set up Tcp socket and spawn threads for connections
        ServerSocket server;
        try {
            server = new ServerSocket(port);
        } catch (IOException e){
            System.err.println("Port " + port + " is already in use");
            return;
        }
        System.out.println("INFO: Listening on " + server.toString());
        while (true) {
            try {
                Socket s = server.accept();
                System.out.println("INFO: Client connected: " + s.toString());
                Scanner socketIn = new Scanner(s.getInputStream());
                PrintWriter socketOut = new PrintWriter(s.getOutputStream());

                if(!controller.isGameReady()){
                    executor.submit(new ClientHandler(s, controller));
                    System.out.println("INFO: Started ClientHandler");
                } else {
                    socketOut.println("Lobby is full, closing connection");
                    socketIn.close();
                    socketOut.close();
                    s.close();
                    System.out.println("INFO: Lobby is full, closing connection");
                }
            } catch (IOException e){
                e.printStackTrace();
                break;
            }
        }
        executor.shutdown();
    }
}
