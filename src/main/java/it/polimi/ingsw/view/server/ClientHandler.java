package it.polimi.ingsw.view.server;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.events.OnServerEventListener;
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
import java.util.*;

/**
 * Handles server side communication with a single client.
 * It will forward events coming from the client to the controller and allows sending server events to the client
 */
public class ClientHandler implements Runnable, OnServerEventListener {
    private final int MISSED_PING_THRESH = 3;
    private final long PING_PERIOD = 5000;

    private final Scanner socketIn;
    private final PrintWriter socketOut;
    private final Socket socket;
    private final GameController controller;
    private final MessageDispatcher dispatcher = new MessageDispatcher();
    private final Timer timer = new Timer();

    private boolean running = true;
    private boolean loggedIn = false;
    private User user = null;
    private int missedPings = 0;

    public ClientHandler(Scanner socketIn, PrintWriter socketOut, GameController controller, Socket socket) {
        this.socket = socket;
        this.socketIn = socketIn;
        this.socketOut = socketOut;
        this.controller = controller;
        controller.addServerEventsListener(this);
        dispatcher.setOnClientEventListener(controller);
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
        setupPing();
        while (running) {
            try {
                Message message = Serializer.deserializeMessage(socketIn.nextLine());

                // Process message
                boolean result = message.visit(dispatcher);
                // Save login information in client handler on successful login
                if(message.getSerializationId() == MessageId.ADD_USER && result) {
                    loggedIn = true;
                    user = ((AddUserMessage)message).getUser();
                }
                // Send result
                if(message.getSerializationId() != MessageId.PING) {
                    sendMessage(new ResultMessage(result));
                }
                logMessageProcessed(message, result);

            } catch (NoSuchElementException e) {
                if (loggedIn && running) {
                    controller.onServerError("User disconnected", user + " disconnected. Terminating.");
                }
                break;
            }
        }
        dispose();
    }

    private void setupPing() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                sendPing();
            }
        }, 5000, PING_PERIOD);
        dispatcher.setOnPingListener(() -> missedPings = 0);
    }

    private void sendPing() {
        if (missedPings >= MISSED_PING_THRESH) {
            if (running && loggedIn) {
                controller.onServerError("Connection to user ", user + " has been lost.");
            } else if (running) {
                sendMessage(new ServerErrorMessage("Failed to respond to pings", "You will be disconnected"));
            }
            running = false;
            socketIn.close();
        }

        sendMessage(new PingMessage());
        missedPings += 1;
    }

    private void dispose() {
        timer.cancel();
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

    @Override
    public void onSizeSelected(int size) {
        Message message = new SizeSelectedMessage(size);
        sendMessage(message);
    }

    private void logMessageProcessed(Message msg, boolean result) {
        if(msg.getSerializationId() != MessageId.PING) { //Don't log PING messages
            CLI.log(CLI.mark(result) + " " + socket.getRemoteSocketAddress() + (user != null ? "/" + user.getUsername() : "") + "/" + msg.getSerializationId());
        }
    }

    private void sendMessage(Message message) {
        String serialized = Serializer.serializeMessage(message);
        synchronized (socketOut) {
            socketOut.println(serialized);
            socketOut.flush();
        }
    }

}
