package it.polimi.ingsw;

import it.polimi.ingsw.view.ProxyView;

import java.io.IOException;


public class Server {
    private String ip;
    private int port;

    public void start() {
        ProxyView proxyView = new ProxyView(ip, port);
        proxyView.start();
    }

    public Server(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }
}
