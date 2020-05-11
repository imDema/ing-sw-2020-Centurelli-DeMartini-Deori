package it.polimi.ingsw.view;

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
import it.polimi.ingsw.view.messages.*;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.*;
import java.util.function.Function;

public class ClientHandler implements Runnable, ServerEventsListener {
    private final Scanner socketIn;
    private final PrintWriter socketOut;
    private final Socket socket;
    private final GameController controller;
    private final Map<MessageId, Function<Message, Boolean>> map = Map.ofEntries(
            new SimpleImmutableEntry<>(MessageId.ADD_USER, this::onAddUser),
            new SimpleImmutableEntry<>(MessageId.CHOOSE_GOD, this::onChooseGod),
            new SimpleImmutableEntry<>(MessageId.PLACE_PAWNS, this::onPlacePawn),
            new SimpleImmutableEntry<>(MessageId.EXECUTE_ACTION, this::onExecuteAction),
            new SimpleImmutableEntry<>(MessageId.CHECK_ACTION, this::onCheckAction));

    private boolean onAddUser(Message message) {
        AddUserMessage msg = (AddUserMessage) message;
        controller.onAddUser(msg.getUser());
        return true;
    }

    private boolean onChooseGod(Message message) {
        ChooseGodMessage msg = (ChooseGodMessage) message;
        controller.onChooseGod(msg.getUser(), msg.getGod());
        return true;
    }

    private boolean onPlacePawn(Message message) {
        PlacePawnsMessage msg = (PlacePawnsMessage) message;
        controller.onPlacePawns(msg.getUser(), msg.getC1(), msg.getC2());
        return true;
    }

    private boolean onExecuteAction(Message message) {
        ExecuteActionMessage msg = (ExecuteActionMessage) message;
        controller.onExecuteAction(msg.getUser(), msg.getId(), msg.getActionIdentifier(), msg.getCoordinate());
        return true;
    }

    private boolean onCheckAction(Message message) {
        CheckActionMessage msg = (CheckActionMessage) message;
        controller.onCheckAction(msg.getUser(), msg.getId(), msg.getActionIdentifier(), msg.getCoordinate());
        return true;
    }

    public ClientHandler(Scanner socketIn, PrintWriter socketOut, GameController controller, Socket socket) {
        this.socket = socket;
        this.socketIn = socketIn;
        this.socketOut = socketOut;
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
        while (true) {
            try {
                Message message = Serializer.deserializeMessage(socketIn.nextLine());
                MessageId id = message.getSerializationId();
                Function<Message, Boolean> handler = map.get(id);
                if (handler != null) {
                    handler.apply(message);
                } else {
                    System.err.println("No handler for MessageId: " + id);
                }
            } catch (NoSuchElementException e) {
                break;
            }
        }
    }



    @Override
    public void onActionsReady(User user, List<ActionIdentifier> actions) {
        Message actionsReadyMsg = new ActionsReadyMessage(user, actions);
        String serializedMessage = Serializer.serializeMessage(actionsReadyMsg);
        synchronized (socketOut) {
            socketOut.println(serializedMessage);
        }
    }

    @Override
    public void onElimination(User user) {
        Message eliminationMsg = new EliminationMessage(user);
        String serializedMessage = Serializer.serializeMessage(eliminationMsg);
        synchronized (socketOut) {
            socketOut.println(serializedMessage);
        }
    }

    @Override
    public void onGodsAvailable(List<GodIdentifier> gods) {
        Message godsAvailableMsg = new GodsAvailableMessage(gods);
        String serializedMessage = Serializer.serializeMessage(godsAvailableMsg);
        synchronized (socketOut) {
            socketOut.println(serializedMessage);
        }
    }

    @Override
    public void onRequestPlacePawns(User user) {
        Message requestPlacePawnsMsg = new RequestPlacePawnsMessage(user);
        String serializedMessage = Serializer.serializeMessage(requestPlacePawnsMsg);
        synchronized (socketOut) {
            socketOut.println(serializedMessage);
        }
    }

    @Override
    public void onServerError(String type, String description) {
        Message serverErrorMsg = new ServerErrorMessage(type, description);
        String serializedMessage = Serializer.serializeMessage(serverErrorMsg);
        synchronized (socketOut) {
            socketOut.println(serializedMessage);
        }
    }

    @Override
    public void onTurnChange(User currentUser, int turn) {
        Message turnChangeMsg = new TurnChangeMessage(currentUser, turn);
        String serializedMessage = Serializer.serializeMessage(turnChangeMsg);
        synchronized (socketOut) {
            socketOut.println(serializedMessage);
        }
    }

    @Override
    public void onWin(User user) {
        Message winMsg = new WinMessage(user);
        String serializedMessage = Serializer.serializeMessage(winMsg);
        synchronized (socketOut) {
            socketOut.println(serializedMessage);
        }
    }

    @Override
    public void onBuild(Building building, Coordinate coordinate) {
        Message buildMsg = new BuildMessage(building, coordinate);
        String serializedMessage = Serializer.serializeMessage(buildMsg);
        synchronized (socketOut) {
            socketOut.println(serializedMessage);
        }
    }

    @Override
    public void onMove(Coordinate from, Coordinate to) {
        Message moveMsg = new MoveMessage(from, to);
        String serializedMessage = Serializer.serializeMessage(moveMsg);
        synchronized (socketOut) {
            socketOut.println(serializedMessage);
        }
    }

    @Override
    public void onGodChosen(User user, GodIdentifier godIdentifier) {
        Message godChosenMsg = new GodChosenMessage(user, godIdentifier);
        String serializedMessage = Serializer.serializeMessage(godChosenMsg);
        synchronized (socketOut) {
            socketOut.println(serializedMessage);
        }
    }

    @Override
    public void onUserJoined(User user) {
        Message userJoinedMsg = new UserJoinedMessage(user);
        String serializedMessage = Serializer.serializeMessage(userJoinedMsg);
        synchronized (socketOut) {
            socketOut.println(serializedMessage);
        }
    }
}
