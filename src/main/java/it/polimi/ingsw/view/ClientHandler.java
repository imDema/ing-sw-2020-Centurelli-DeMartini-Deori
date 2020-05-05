package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.GameController;

import java.io.*;

public class ClientHandler implements Runnable {
    private final InputStream strin;
    private final OutputStream strout;
    private final GameController controller;


    public ClientHandler(InputStream strin, OutputStream strout, GameController controller) {
        // TODO: Use buffered readers
        this.strin = strin;
        this.strout = strout;
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
            strout.write("Allo!".getBytes());
        } catch (IOException e) {
            // TODO: IMPORTANT, Handle connection error
            e.printStackTrace();
        }
    }
}
