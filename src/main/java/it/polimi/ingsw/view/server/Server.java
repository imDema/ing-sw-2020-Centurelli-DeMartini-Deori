package it.polimi.ingsw.view.server;

/**
 * Santorini game server
 */
public class Server {
    private final String ip;
    private final int port;

    public void start() {
        ProxyView proxyView = new ProxyView(ip, port);
        proxyView.start();
    }

    public Server(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }
}
