package it.polimi.ingsw.view;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ProxyController {
    private final String ip;
    private final int port;

    public ProxyController(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public ServerHandler start() throws IOException {
        Socket socket = new Socket(ip, port);
        Scanner socketIn = new Scanner(socket.getInputStream());
        PrintWriter socketOut = new PrintWriter(socket.getOutputStream());
        ServerHandler serverHandler = new ServerHandler(socketIn, socketOut, socket);
        serverHandler.run();
        return serverHandler;




    }

}
