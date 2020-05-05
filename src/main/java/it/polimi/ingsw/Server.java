package it.polimi.ingsw;

import it.polimi.ingsw.view.ProxyView;

import java.io.IOException;


public class Server {
    public static void main(String[] args) throws IOException {
        //TODO, TEMP
        String ip = args[1];
        int port = Integer.parseInt(args[2]);

        ProxyView proxyView = new ProxyView(ip, port);
        proxyView.start();
    }
}
