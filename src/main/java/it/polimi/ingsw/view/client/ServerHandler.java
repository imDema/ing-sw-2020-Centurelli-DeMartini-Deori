package it.polimi.ingsw.view.client;

import it.polimi.ingsw.controller.events.*;
import it.polimi.ingsw.controller.messages.ActionIdentifier;
import it.polimi.ingsw.controller.messages.GodIdentifier;
import it.polimi.ingsw.controller.messages.User;
import it.polimi.ingsw.model.board.Coordinate;
import it.polimi.ingsw.model.board.events.OnBuildListener;
import it.polimi.ingsw.model.board.events.OnMoveListener;
import it.polimi.ingsw.serialization.Serializer;
import it.polimi.ingsw.view.cli.CLI;
import it.polimi.ingsw.view.events.ClientEventsListener;
import it.polimi.ingsw.view.messages.*;

import java.io.IOException;
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
    private OnGodsAvailableListener godsAvailableListener = null;
    private OnGodChosenListener godChosenListener = null;
    private OnActionsReadyListener actionsReadyListener = null;
    private OnEliminationListener eliminationListener = null;
    private OnRequestPlacePawnsListener requestPlacePawnsListener = null;
    private OnServerErrorListener serverErrorListener = null;
    private OnTurnChangeListener turnChangeListener = null;
    private OnWinListener winListener = null;
    private OnUserJoinedListener userJoinedListener = null;
    private OnResultListener resultListener = null;
    private OnMoveListener moveListener = null;
    private OnBuildListener buildListener = null;
    private OnPawnPlacedListener pawnPlacedListener = null;
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
            new SimpleImmutableEntry<>(MessageId.PAWN_PLACED, this::onPawnPlaced),
            new SimpleImmutableEntry<>(MessageId.RESULT, this::onResult) );

    public ServerHandler(Scanner socketIn, PrintWriter socketOut, Socket socket) {
        this.socketIn = socketIn;
        this.socketOut = socketOut;
        this.socket = socket;
    }

    public void setGodsAvailableListener(OnGodsAvailableListener godsAvailableListener) {
        this.godsAvailableListener = godsAvailableListener;
    }

    public void setGodChosenListener(OnGodChosenListener godChosenListener) {
        this.godChosenListener = godChosenListener;
    }

    public void setActionsReadyListener(OnActionsReadyListener actionsReadyListener) {
        this.actionsReadyListener = actionsReadyListener;
    }

    public void setEliminationListener(OnEliminationListener eliminationListener) {
        this.eliminationListener = eliminationListener;
    }

    public void setRequestPlacePawnsListener(OnRequestPlacePawnsListener requestPlacePawnsListener) {
        this.requestPlacePawnsListener = requestPlacePawnsListener;
    }

    public void setServerErrorListener(OnServerErrorListener serverErrorListener) {
        this.serverErrorListener = serverErrorListener;
    }

    public void setTurnChangeListener(OnTurnChangeListener turnChangeListener) {
        this.turnChangeListener = turnChangeListener;
    }

    public void setWinListener(OnWinListener winListener) {
        this.winListener = winListener;
    }

    public void setUserJoinedListener(OnUserJoinedListener userJoinedListener) {
        this.userJoinedListener = userJoinedListener;
    }

    public void setResultListener(OnResultListener resultListener) {
        this.resultListener = resultListener;
    }

    public void setMoveListener(OnMoveListener moveListener) {
        this.moveListener = moveListener;
    }

    public void setBuildListener(OnBuildListener buildListener) {
        this.buildListener = buildListener;
    }

    public void setPawnPlacedListener(OnPawnPlacedListener pawnPlacedListener) {
        this.pawnPlacedListener = pawnPlacedListener;
    }

    private void onGodsAvailable (Message message) {
        if(godsAvailableListener != null) {
            GodsAvailableMessage msg = (GodsAvailableMessage) message;
            godsAvailableListener.onGodsAvailable(msg.getGods());
        }
    }

    private void onGodChosen (Message message) {
        if (godChosenListener != null) {
            GodChosenMessage msg = (GodChosenMessage) message;
            godChosenListener.onGodChosen(msg.getUser(), msg.getGodIdentifier());
        }
    }

    private void onActionsReady(Message message) {
        if(actionsReadyListener != null) {
            ActionsReadyMessage msg = (ActionsReadyMessage) message;
            actionsReadyListener.onActionsReady(msg.getUser(), msg.getActionIdentifiers());
        }
    }

    private void onElimination(Message message) {
        if(eliminationListener != null) {
            EliminationMessage msg = (EliminationMessage) message;
            eliminationListener.onElimination(msg.getUser());
        }
    }

    private void onRequestPlacePawns(Message message) {
        if(requestPlacePawnsListener != null) {
            RequestPlacePawnsMessage msg = (RequestPlacePawnsMessage) message;
            requestPlacePawnsListener.onRequestPlacePawns(msg.getUser());
        }
    }

    private void onServerError(Message message) {
        if(serverErrorListener != null) {
            ServerErrorMessage msg = (ServerErrorMessage) message;
            serverErrorListener.onServerError(msg.getType(), msg.getDescription());
        }
    }

    private void onTurnChange(Message message) {
        if(turnChangeListener != null) {
            TurnChangeMessage msg = (TurnChangeMessage) message;
            turnChangeListener.onTurnChange(msg.getUser(), msg.getTurn());
        }
    }

    private void onWin(Message message) {
        if(winListener != null) {
            WinMessage msg = (WinMessage) message;
            winListener.onWin(msg.getUser());
        }
    }

    private void onUserJoined(Message message) {
        if(userJoinedListener != null) {
            UserJoinedMessage msg = (UserJoinedMessage) message;
            userJoinedListener.onUserJoined(msg.getUser());
        }
    }

    private void onMove(Message message) {
        if(moveListener != null) {
            MoveMessage msg = (MoveMessage) message;
            moveListener.onMove(msg.getSource(), msg.getDestination());
        }
    }

    private void onBuild(Message message) {
        if(buildListener != null) {
            BuildMessage msg = (BuildMessage) message;
            buildListener.onBuild(msg.getBuilding(), msg.getCoordinate());
        }
    }

    private void onPawnPlaced(Message message) {
        if(pawnPlacedListener != null) {
            PawnPlacedMessage msg = (PawnPlacedMessage) message;
            pawnPlacedListener.onPawnPlaced(msg.getOwner(), msg.getPawnId(), msg.getCoordinate());
        }
    }

    private void onResult(Message message) {
        if(resultListener != null){
            ResultMessage msg = (ResultMessage) message;
            resultListener.onResult(msg.getValue());
        }
    }

    public void setOnResultListener(OnResultListener onResultListener) {
        this.resultListener = onResultListener;
    }

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
    public boolean onSelectPlayerNumber(int size) {
        Message message = new SelectPlayerNumberMessage(size);
        sendMessage(message);
        return true;
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
