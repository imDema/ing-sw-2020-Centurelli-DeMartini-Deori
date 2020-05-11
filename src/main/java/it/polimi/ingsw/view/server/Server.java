package it.polimi.ingsw.view.server;

import it.polimi.ingsw.view.server.ProxyView;


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
