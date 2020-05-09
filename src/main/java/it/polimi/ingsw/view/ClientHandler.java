package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.GameController;

import java.io.IOException;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private final Socket socket;
    private final GameController controller;


    public ClientHandler(Socket socket, GameController controller) {
        this.socket = socket;
        this.controller = controller;
    }

    /**
     * When an object implementing interface {@code Runnable} is used
     * to create a thread, starting the thread causes the object's
     * {@code run} method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method {@code run} is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        try {
            socket.getOutputStream().write("Allo!".getBytes());
        } catch (IOException e) {
            // TODO: IMPORTANT, Handle connection error
            e.printStackTrace();
        }
    }
}
