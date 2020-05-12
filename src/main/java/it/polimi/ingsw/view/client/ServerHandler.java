package it.polimi.ingsw.view.client;

import it.polimi.ingsw.controller.events.OnResultListener;
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
import java.util.Scanner;
import java.util.function.Consumer;

public class ServerHandler implements Runnable, ClientEventsListener {
    private final Scanner socketIn;
    private final PrintWriter socketOut;
    private final Socket socket;
    private ServerEventsListener serverEventsListener = null;
    private final Map<MessageId, Consumer<Message>> map = Map.ofEntries(
            new SimpleImmutableEntry<>(MessageId.GODS_AVAILABLE, this::onGodsAvailable),
            new SimpleImmutableEntry<>(MessageId.GOD_CHOSEN, this::onGodChosen),
            new SimpleImmutableEntry<>(MessageId.ACTION_READY, this::onActionsReady),
            new SimpleImmutableEntry<>(MessageId.ELIMINATION, this::onElimination),
            new SimpleImmutableEntry<>(MessageId.REQUEST_PLACE_PAWNS, this::onRequestPlacePawns),
            new SimpleImmutableEntry<>(MessageId.SERVER_ERROR, this::onServerError),
            new SimpleImmutableEntry<>(MessageId.TURN_CHANGE, this::onTurnChange),
            new SimpleImmutableEntry<>(MessageId.WIN, this::onWin),
            new SimpleImmutableEntry<>(MessageId.USER_JOINED, this::onUserJoined),
            new SimpleImmutableEntry<>(MessageId.RESULT, this::onResult),
            new SimpleImmutableEntry<>(MessageId.MOVE, this::onMove),
            new SimpleImmutableEntry<>(MessageId.BUILD, this::onBuild),
            new SimpleImmutableEntry<>(MessageId.PAWN_PLACED, this::onPawnPlaced)
    );
    private OnResultListener resultListener = null;

    public ServerHandler(Scanner socketIn, PrintWriter socketOut, Socket socket) {
        this.socketIn = socketIn;
        this.socketOut = socketOut;
        this.socket = socket;
    }

    public void setOnResultListener(OnResultListener onResultListener) {
        this.resultListener = onResultListener;
    }

    private void onGodsAvailable (Message message){
        GodsAvailableMessage msg = (GodsAvailableMessage) message;
        serverEventsListener.onGodsAvailable(msg.getGods());
    }

    private void onGodChosen (Message message){
        GodChosenMessage msg = (GodChosenMessage) message;
        serverEventsListener.onGodChosen(msg.getUser(),msg.getGodIdentifier());
    }

    private void onActionsReady(Message message){
        ActionsReadyMessage msg = (ActionsReadyMessage) message;
        serverEventsListener.onActionsReady(msg.getUser(), msg.getActionIdentifiers());
    }

    private void onElimination(Message message){
        EliminationMessage msg = (EliminationMessage) message;
        serverEventsListener.onElimination(msg.getUser());
    }

    private void onRequestPlacePawns(Message message){
        RequestPlacePawnsMessage msg = (RequestPlacePawnsMessage) message;
        serverEventsListener.onRequestPlacePawns(msg.getUser());
    }

    private void onServerError(Message message){
        ServerErrorMessage msg = (ServerErrorMessage) message;
        serverEventsListener.onServerError(msg.getType(), msg.getDescription());
    }

    private void onTurnChange(Message message){
        TurnChangeMessage msg = (TurnChangeMessage) message;
        serverEventsListener.onTurnChange(msg.getUser(), msg.getTurn());
    }

    private void onWin(Message message){
        WinMessage msg = (WinMessage) message;
        serverEventsListener.onWin(msg.getUser());
    }

    private void onUserJoined(Message message){
        UserJoinedMessage msg = (UserJoinedMessage) message;
        serverEventsListener.onUserJoined(msg.getUser());
    }

    private void onMove(Message message){
        MoveMessage msg = (MoveMessage) message;
        serverEventsListener.onMove(msg.getSource(), msg.getDestination());
    }

    private void onBuild(Message message){
        BuildMessage msg = (BuildMessage) message;
        serverEventsListener.onBuild(msg.getBuilding(), msg.getCoordinate());
    }

    private void onPawnPlaced(Message message){
        PawnPlacedMessage msg = (PawnPlacedMessage) message;
        serverEventsListener.onPawnPlaced(msg.getOwner(), msg.getPawnId(), msg.getCoordinate());
    }

    private void onResult(Message message){
        ResultMessage msg = (ResultMessage) message;
        resultListener.onResult(msg.getValue());
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
                Consumer<Message> handler = map.get(id);
                if(handler != null){
                    handler.accept(message);
                } else {
                    System.err.println("No handler for MessageId: " + id);
                }
            } catch (NoSuchElementException e) {
                break;
            }
        }
    }

    @Override
    public boolean onAddUser(User user) {
        Message message = new AddUserMessage(user);
        sendMessage(message);
        return true;
    }

    @Override
    public boolean onChooseGod(User user, GodIdentifier god) {
        Message message = new ChooseGodMessage(user, god);
        sendMessage(message);
        return true;
    }

    @Override
    public boolean onPlacePawns(User user, Coordinate c1, Coordinate c2) {
        Message message = new PlacePawnsMessage(user, c1, c2);
        sendMessage(message);
        return true;
    }

    @Override
    public boolean onCheckAction(User user, int pawnId, ActionIdentifier actionIdentifier, Coordinate coordinate) {
        Message message = new CheckActionMessage(user, pawnId, actionIdentifier, coordinate);
        sendMessage(message);
        return true;
    }

    @Override
    public boolean onExecuteAction(User user, int pawnId, ActionIdentifier actionIdentifier, Coordinate coordinate) {
        Message message = new ExecuteActionMessage(user, pawnId, actionIdentifier, coordinate);
        sendMessage(message);
        return true;
    }

    public void sendMessage(Message message ){
        String serialized = Serializer.serializeMessage(message);
        synchronized (socketOut){
            socketOut.println(serialized);
            socketOut.flush();
        }
    }
}
