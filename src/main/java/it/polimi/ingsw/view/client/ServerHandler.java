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
    private boolean running = true;
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
            new SimpleImmutableEntry<>(MessageId.MOVE, this::onMove),
            new SimpleImmutableEntry<>(MessageId.BUILD, this::onBuild),
            new SimpleImmutableEntry<>(MessageId.PAWN_PLACED, this::onPawnPlaced),
            new SimpleImmutableEntry<>(MessageId.RESULT, this::onResult));

    public ServerHandler(Scanner socketIn, PrintWriter socketOut, Socket socket) {
        this.socketIn = socketIn;
        this.socketOut = socketOut;
        this.socket = socket;
    }

    public void setOnGodsAvailableListener(OnGodsAvailableListener godsAvailableListener) {
        this.godsAvailableListener = godsAvailableListener;
    }

    public void setOnGodChosenListener(OnGodChosenListener godChosenListener) {
        this.godChosenListener = godChosenListener;
    }

    public void setOnActionsReadyListener(OnActionsReadyListener actionsReadyListener) {
        this.actionsReadyListener = actionsReadyListener;
    }

    public void setOnEliminationListener(OnEliminationListener eliminationListener) {
        this.eliminationListener = eliminationListener;
    }

    public void setOnRequestPlacePawnsListener(OnRequestPlacePawnsListener requestPlacePawnsListener) {
        this.requestPlacePawnsListener = requestPlacePawnsListener;
    }

    public void setOnServerErrorListener(OnServerErrorListener serverErrorListener) {
        this.serverErrorListener = serverErrorListener;
    }

    public void setOnTurnChangeListener(OnTurnChangeListener turnChangeListener) {
        this.turnChangeListener = turnChangeListener;
    }

    public void setOnWinListener(OnWinListener winListener) {
        this.winListener = winListener;
    }

    public void setOnUserJoinedListener(OnUserJoinedListener userJoinedListener) {
        this.userJoinedListener = userJoinedListener;
    }

    public void setOnResultListener(OnResultListener resultListener) {
        this.resultListener = resultListener;
    }

    public void setOnMoveListener(OnMoveListener moveListener) {
        this.moveListener = moveListener;
    }

    public void setOnBuildListener(OnBuildListener buildListener) {
        this.buildListener = buildListener;
    }

    public void setOnPawnPlacedListener(OnPawnPlacedListener pawnPlacedListener) {
        this.pawnPlacedListener = pawnPlacedListener;
    }

    public void setServerEventListener(ServerEventsListener serverEventsListener) {
        this.godsAvailableListener = serverEventsListener;
        this.godChosenListener = serverEventsListener;
        this.actionsReadyListener = serverEventsListener;
        this.eliminationListener = serverEventsListener;
        this.requestPlacePawnsListener = serverEventsListener;
        this.serverErrorListener = serverEventsListener;
        this.turnChangeListener = serverEventsListener;
        this.winListener = serverEventsListener;
        this.userJoinedListener = serverEventsListener;
        this.moveListener = serverEventsListener;
        this.buildListener = serverEventsListener;
        this.pawnPlacedListener = serverEventsListener;
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
                    CLI.error("No handler for MessageId: " + id);
                }
            } catch (NoSuchElementException e) {
                if (running) {
                    if (serverErrorListener != null)
                        serverErrorListener.onServerError("Connection error", "The connection to the server has been lost");
                }
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

    public void stop() {
        running = false;
        socketOut.close();
        socketIn.close();
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
