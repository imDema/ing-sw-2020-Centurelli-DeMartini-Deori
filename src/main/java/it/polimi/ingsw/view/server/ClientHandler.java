package it.polimi.ingsw.view.server;

// read socket messages
// identify messages with MessageId
// map messages to the controller function (Using java.util.Map)
// synchronized(socket stream)

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
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.function.Consumer;

public class ClientHandler implements Runnable, ServerEventsListener {
    private final Scanner socketIn;
    private final PrintWriter socketOut;
    private final Socket socket;
    private final GameController controller;
    private final Map<MessageId, Consumer<Message>> map = Map.ofEntries(
            new SimpleImmutableEntry<>(MessageId.ADD_USER, this::onAddUser),
            new SimpleImmutableEntry<>(MessageId.CHOOSE_GOD, this::onChooseGod),
            new SimpleImmutableEntry<>(MessageId.PLACE_PAWNS, this::onPlacePawn),
            new SimpleImmutableEntry<>(MessageId.EXECUTE_ACTION, this::onExecuteAction),
            new SimpleImmutableEntry<>(MessageId.SELECT_PLAYER_NUMBER, this::onSelectPlayerNumber),
            new SimpleImmutableEntry<>(MessageId.CHECK_ACTION, this::onCheckAction));

    private boolean running = true;
    private boolean loggedIn = false;
    private User user = null;

    private void onSelectPlayerNumber(Message message) {
        SelectPlayerNumberMessage msg = (SelectPlayerNumberMessage) message;
        boolean result = controller.onSelectPlayerNumber(msg.getSize());
        sendMessage(new ResultMessage(result));
        logMessageProcessed(msg, result);
    }

    private void onAddUser(Message message) {
        AddUserMessage msg = (AddUserMessage) message;
        boolean result = controller.onAddUser(msg.getUser());
        // Save user information
        if(result) {
            loggedIn = true;
            user = msg.getUser();
        }
        sendMessage(new ResultMessage(result));
        logMessageProcessed(msg, result);
    }

    private void onChooseGod(Message message) {
        ChooseGodMessage msg = (ChooseGodMessage) message;
        boolean result = controller.onChooseGod(msg.getUser(), msg.getGod());
        sendMessage(new ResultMessage(result));
        logMessageProcessed(msg, result);
    }

    private void onPlacePawn(Message message) {
        PlacePawnsMessage msg = (PlacePawnsMessage) message;
        boolean result = controller.onPlacePawns(msg.getUser(), msg.getC1(), msg.getC2());
        sendMessage(new ResultMessage(result));
        logMessageProcessed(msg, result);
    }

    private void onExecuteAction(Message message) {
        ExecuteActionMessage msg = (ExecuteActionMessage) message;
        boolean result = controller.onExecuteAction(msg.getUser(), msg.getId(), msg.getActionIdentifier(), msg.getCoordinate());
        sendMessage(new ResultMessage(result));
        logMessageProcessed(msg, result);
    }

    private void onCheckAction(Message message) {
        CheckActionMessage msg = (CheckActionMessage) message;
        boolean result = controller.onCheckAction(msg.getUser(), msg.getId(), msg.getActionIdentifier(), msg.getCoordinate());
        sendMessage(new ResultMessage(result));
        logMessageProcessed(msg, result);
    }

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
                Consumer<Message> handler = map.get(id);
                if (handler != null) {
                    handler.accept(message);
                } else {
                    System.err.println("No handler for MessageId: " + id);
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
