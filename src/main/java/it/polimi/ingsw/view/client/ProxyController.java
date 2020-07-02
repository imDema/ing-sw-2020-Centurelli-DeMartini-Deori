package it.polimi.ingsw.view.client;

import it.polimi.ingsw.view.cli.CLI;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

/**
 * Virtual controller used by clients.
 */
public class ProxyController {
    private final String ip;
    private final int port;

    public ProxyController(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    /**
     * Try connecting to the server
     * @return ServerHandler used to communicate with the server
     * @throws IOException If there was an error connecting to the socket
     */
    public ServerHandler start() throws IOException {
        CLI.info("Connecting to server at ip: " + ip + " port: " + port);
        InetAddress address = InetAddress.getByName(ip);
        Socket socket = new Socket(address, port);
        socket.setSoTimeout(10000);
        Scanner socketIn = new Scanner(socket.getInputStream());
        PrintWriter socketOut = new PrintWriter(socket.getOutputStream());
        return new ServerHandler(socketIn, socketOut, socket);
    }
}
