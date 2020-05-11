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
            new SimpleImmutableEntry<>(MessageId.CHECK_ACTION, this::onCheckAction));

    private void onAddUser(Message message) {
        AddUserMessage msg = (AddUserMessage) message;
        System.out.println("INFO: ADD_USER: " + msg.getUser().getUsername());
        controller.onAddUser(msg.getUser());
    }

    private void onChooseGod(Message message) {
        ChooseGodMessage msg = (ChooseGodMessage) message;
        controller.onChooseGod(msg.getUser(), msg.getGod());
    }

    private void onPlacePawn(Message message) {
        PlacePawnsMessage msg = (PlacePawnsMessage) message;
        controller.onPlacePawns(msg.getUser(), msg.getC1(), msg.getC2());
    }

    private void onExecuteAction(Message message) {
        ExecuteActionMessage msg = (ExecuteActionMessage) message;
        controller.onExecuteAction(msg.getUser(), msg.getId(), msg.getActionIdentifier(), msg.getCoordinate());
    }

    private void onCheckAction(Message message) {
        CheckActionMessage msg = (CheckActionMessage) message;
        controller.onCheckAction(msg.getUser(), msg.getId(), msg.getActionIdentifier(), msg.getCoordinate());
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
        while (true) {
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
        Message actionsReadyMsg = new ActionsReadyMessage(user, actions);
        sendMessage(actionsReadyMsg);
    }

    @Override
    public void onElimination(User user) {
        Message eliminationMsg = new EliminationMessage(user);
        sendMessage(eliminationMsg);
    }

    @Override
    public void onGodsAvailable(List<GodIdentifier> gods) {
        Message godsAvailableMsg = new GodsAvailableMessage(gods);
        sendMessage(godsAvailableMsg);
    }

    @Override
    public void onRequestPlacePawns(User user) {
        Message requestPlacePawnsMsg = new RequestPlacePawnsMessage(user);
        sendMessage(requestPlacePawnsMsg);
    }

    @Override
    public void onServerError(String type, String description) {
        Message serverErrorMsg = new ServerErrorMessage(type, description);
        sendMessage(serverErrorMsg);
    }

    @Override
    public void onTurnChange(User currentUser, int turn) {
        Message turnChangeMsg = new TurnChangeMessage(currentUser, turn);
        sendMessage(turnChangeMsg);
    }

    @Override
    public void onWin(User user) {
        Message winMsg = new WinMessage(user);
        sendMessage(winMsg);
    }

    @Override
    public void onBuild(Building building, Coordinate coordinate) {
        Message buildMsg = new BuildMessage(building, coordinate);
        sendMessage(buildMsg);
    }

    @Override
    public void onMove(Coordinate from, Coordinate to) {
        Message moveMsg = new MoveMessage(from, to);
        sendMessage(moveMsg);
    }

    @Override
    public void onGodChosen(User user, GodIdentifier godIdentifier) {
        Message godChosenMsg = new GodChosenMessage(user, godIdentifier);
        sendMessage(godChosenMsg);
    }

    @Override
    public void onUserJoined(User user) {
        Message userJoinedMsg = new UserJoinedMessage(user);
        sendMessage(userJoinedMsg);
    }

    public void sendMessage(Message message) {
        String serialized = Serializer.serializeMessage(message);
        synchronized (socketOut) {
            socketOut.println(serialized);
            socketOut.flush();
        }
    }
}
