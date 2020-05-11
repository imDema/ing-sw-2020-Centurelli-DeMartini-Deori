package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.events.ServerEventsListener;
import it.polimi.ingsw.controller.messages.ActionIdentifier;
import it.polimi.ingsw.controller.messages.GodIdentifier;
import it.polimi.ingsw.controller.messages.User;
import it.polimi.ingsw.model.board.Coordinate;
import it.polimi.ingsw.serialization.Serializer;
import it.polimi.ingsw.view.events.ClientEventsListener;
import it.polimi.ingsw.view.messages.*;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Scanner;
import java.util.function.Function;

public class ServerHandler implements Runnable, ClientEventsListener {
    private final Scanner socketIn;
    private final PrintWriter socketOut;
    private final Socket socket;
    private ServerEventsListener serverEventsListener = null;
    private final Map<MessageId, Function<Message, Boolean>> map = Map.ofEntries(
            new SimpleImmutableEntry<>(MessageId.GODS_AVAILABLE, this::onGodsAvailable),
            new SimpleImmutableEntry<>(MessageId.ACTION_READY, this::onActionsReady),
            new SimpleImmutableEntry<>(MessageId.ELIMINATION, this::onElimination),
            new SimpleImmutableEntry<>(MessageId.REQUEST_PLACE_PAWNS, this::onRequestPlacePawns),
            new SimpleImmutableEntry<>(MessageId.SERVER_ERROR, this::onServerError ),
            new SimpleImmutableEntry<>(MessageId.TURN_CHANGE, this::onTurnChange ),
            new SimpleImmutableEntry<>(MessageId.WIN, this::onWin ),
            new SimpleImmutableEntry<>(MessageId.USER_JOINED, this::onUserJoined ));

    public ServerHandler(Scanner socketIn, PrintWriter socketOut, Socket socket) {
        this.socketIn = socketIn;
        this.socketOut = socketOut;
        this.socket = socket;
    }

    private Boolean onGodsAvailable (Message message){
        GodsAvailableMessage msg = (GodsAvailableMessage) message;
        serverEventsListener.onGodsAvailable(msg.getGods());
        return true;
    }

    private Boolean onActionsReady(Message message){
        ActionsReadyMessage msg = (ActionsReadyMessage) message;
        serverEventsListener.onActionsReady(msg.getUser(), msg.getActionIdentifiers());
        return true;
    }

    private Boolean onElimination(Message message){
        EliminationMessage msg = (EliminationMessage) message;
        serverEventsListener.onElimination(msg.getUser());
        return true;
    }

    private Boolean onRequestPlacePawns(Message message){
        RequestPlacePawnsMessage msg =(RequestPlacePawnsMessage) message;
        serverEventsListener.onRequestPlacePawns(msg.getUser());
        return true;
    }

    private Boolean onServerError(Message message){
        ServerErrorMessage msg =(ServerErrorMessage) message;
        serverEventsListener.onServerError(msg.getType(), msg.getDescription());
        return true;
    }

    private Boolean onTurnChange(Message message){
        TurnChangeMessage msg =(TurnChangeMessage) message;
        serverEventsListener.onTurnChange(msg.getUser(), msg.getTurn());
        return true;
    }

    private Boolean onWin(Message message){
        WinMessage msg =(WinMessage) message;
        serverEventsListener.onWin(msg.getUser());
        return true;
    }

    private Boolean onUserJoined(Message message){
        UserJoinedMessage msg =(UserJoinedMessage) message;
        serverEventsListener.onWin(msg.getUser());
        return true;
    }

    public void setServerEventsListener(ServerEventsListener serverEventsListener) {
        this.serverEventsListener = serverEventsListener;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Message message = Serializer.deserializeMessage(socketIn.nextLine());
                MessageId id = message.getSerializationId();
                Function<Message, Boolean> handler = map.get(id);
                if(handler != null){
                    handler.apply(message);
                } else {
                    System.err.println("No handler for MessageId: " + id);
                }
            } catch (NoSuchElementException e) {
                break;
            }
        }
    }

    // Serialize, synchronize, send
    @Override
    public Optional<User> onAddUser(String username) {
        return Optional.empty();
    }

    @Override
    public boolean onChooseGod(User user, GodIdentifier god) {
        return false;
    }

    @Override
    public boolean onPlacePawns(User user, Coordinate c1, Coordinate c2) {
        return false;
    }

    @Override
    public boolean onCheckAction(User user, int pawnId, ActionIdentifier actionIdentifier, Coordinate coordinate) {
        return false;
    }

    @Override
    public boolean onExecuteAction(User user, int pawnId, ActionIdentifier actionIdentifier, Coordinate coordinate) {
        return false;
    }
}
