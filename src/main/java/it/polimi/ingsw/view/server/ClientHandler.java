package it.polimi.ingsw.view.server;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.events.ServerEventsListener;
import it.polimi.ingsw.controller.messages.ActionIdentifier;
import it.polimi.ingsw.controller.messages.GodIdentifier;
import it.polimi.ingsw.controller.messages.User;
import it.polimi.ingsw.model.board.Building;
import it.polimi.ingsw.model.board.Coordinate;
import it.polimi.ingsw.serialization.Serializer;
import it.polimi.ingsw.view.cli.CLI;
import it.polimi.ingsw.view.messages.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ClientHandler implements Runnable, ServerEventsListener {
    private final Scanner socketIn;
    private final PrintWriter socketOut;
    private final Socket socket;
    private final GameController controller;

    private boolean running = true;
    private boolean loggedIn = false;
    private User user = null;

    public ClientHandler(Scanner socketIn, PrintWriter socketOut, GameController controller, Socket socket) {
        this.socket = socket;
        this.socketIn = socketIn;
        this.socketOut = socketOut;
        this.controller = controller;
        controller.addServerEventsListener(this);
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
        while (running) {
            try {
                Message message = Serializer.deserializeMessage(socketIn.nextLine());
                MessageId id = message.getSerializationId();

                if (id.clientMessage()) {
                    // Process message
                    boolean result = ((ClientMessage) message).visit(controller);

                    // Save login information in client handler on successful login
                    if(id == MessageId.ADD_USER && result) {
                        loggedIn = true;
                        user = ((AddUserMessage)message).getUser();
                    }

                    // Send result
                    sendMessage(new ResultMessage(result));
                    logMessageProcessed(message, result);
                } else {
                    CLI.error("Cannot process ServerMessage " + message.getSerializationId());
                }
            } catch (NoSuchElementException e) {
                if (loggedIn && running) {
                    controller.onServerError("User disconnected", user + " disconnected. Terminating.");
                }
                break;
            }
        }
        controller.removeServerEventsListener(this);
        socketOut.close();
        socketIn.close();
        try {
            socket.close();
        } catch (IOException e) {
            CLI.error("Exception thrown while closing socket");
            e.printStackTrace();
        }
    }


    @Override
    public void onActionsReady(User user, List<ActionIdentifier> actions) {
        Message message = new ActionsReadyMessage(user, actions);
        sendMessage(message);
    }

    @Override
    public void onElimination(User user) {
        Message message = new EliminationMessage(user);
        sendMessage(message);
    }

    @Override
    public void onGodsAvailable(List<GodIdentifier> gods) {
        Message message = new GodsAvailableMessage(gods);
        sendMessage(message);
    }

    @Override
    public void onRequestPlacePawns(User user) {
        Message message = new RequestPlacePawnsMessage(user);
        sendMessage(message);
    }

    @Override
    public void onServerError(String type, String description) {
        Message message = new ServerErrorMessage(type, description);
        sendMessage(message);
        running = false;
    }

    @Override
    public void onTurnChange(User currentUser, int turn) {
        Message message = new TurnChangeMessage(currentUser, turn);
        sendMessage(message);
    }

    @Override
    public void onWin(User user) {
        Message message = new WinMessage(user);
        sendMessage(message);
        running = false;
    }

    @Override
    public void onBuild(Building building, Coordinate coordinate) {
        Message message = new BuildMessage(building, coordinate);
        sendMessage(message);
    }

    @Override
    public void onMove(Coordinate from, Coordinate to) {
        Message message = new MoveMessage(from, to);
        sendMessage(message);
    }

    @Override
    public void onGodChosen(User user, GodIdentifier godIdentifier) {
        Message message = new GodChosenMessage(user, godIdentifier);
        sendMessage(message);
    }

    @Override
    public void onUserJoined(User user) {
        Message message = new UserJoinedMessage(user);
        sendMessage(message);
    }

    @Override
    public void onPawnPlaced(User owner, int pawnId, Coordinate coordinate) {
        Message message = new PawnPlacedMessage(owner, pawnId, coordinate);
        sendMessage(message);
    }

    private void logMessageProcessed(Message msg, boolean result) {
        CLI.log(CLI.mark(result) + " " + (user != null ? user.getUsername() : "") + "/" + msg.getSerializationId());
    }

    public void sendMessage(Message message) {
        String serialized = Serializer.serializeMessage(message);
        synchronized (socketOut) {
            socketOut.println(serialized);
            socketOut.flush();
        }
    }

}
