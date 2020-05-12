package it.polimi.ingsw.view.client;

import it.polimi.ingsw.view.cli.CLI;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
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
        CLI.info("Connecting to server at ip: " + ip + " port: " + port);
        InetAddress address = InetAddress.getByName(ip);
        Socket socket = new Socket(address, port);
        Scanner socketIn = new Scanner(socket.getInputStream());
        PrintWriter socketOut = new PrintWriter(socket.getOutputStream());
        return new ServerHandler(socketIn, socketOut, socket);
    }
}
