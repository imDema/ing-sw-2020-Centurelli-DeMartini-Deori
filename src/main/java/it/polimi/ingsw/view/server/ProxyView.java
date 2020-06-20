package it.polimi.ingsw.view.server;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.events.OnGameFinishedListener;
import it.polimi.ingsw.view.cli.CLI;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ProxyView implements OnGameFinishedListener {
    private final String ip;
    private final int port;
    private final ExecutorService executor = Executors.newCachedThreadPool();
    private GameController controller;


    public ProxyView(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public void start() {
        // Set up Tcp socket and spawn threads for connections
        ServerSocket server;
        try {
            InetAddress address = InetAddress.getByName(ip);
            server = new ServerSocket(port, 8, address);
        } catch (UnknownHostException e) {
            System.err.println("Invalid address: " + ip);
            return;
        }
        catch (IOException e){
            System.err.println("Port " + port + " is already in use");
            return;
        }
        CLI.info("Listening on " + server.toString());
        controller = startLobby();
        CLI.info("Started a new lobby " + this.controller.getUidShortString());
        while (true) {
            try {
                Socket s = server.accept();
                CLI.info("Client connected: " + s.toString());
                Scanner socketIn = new Scanner(s.getInputStream());
                PrintWriter socketOut = new PrintWriter(s.getOutputStream());

                if(controller.isLobbyFull()) {
                    controller = startLobby();
                    CLI.info("Current lobby is full. Started a new one " + controller.getUidShortString());
                }
                executor.submit(new ClientHandler(socketIn, socketOut, controller, s));
            } catch (IOException e){
                e.printStackTrace();
                break;
            }
        }
        executor.shutdown();
    }

    private GameController startLobby() {
        GameController controller = new GameController();
        controller.setGameFinishedListener(this);
        return controller;
    }

    @Override
    public void onGameFinished(GameController controller) {
        CLI.info("Lobby " + controller.getUidShortString() + " has finished");

        // Start a new controller to avoid giving new clients a stale controller
        if (controller.equals(this.controller)) {
            this.controller = startLobby();
            CLI.info("Started a new lobby " + this.controller.getUidShortString());
        }
    }
}
