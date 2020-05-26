package it.polimi.ingsw.view.client.state;

import it.polimi.ingsw.controller.messages.ActionIdentifier;
import it.polimi.ingsw.controller.messages.User;
import it.polimi.ingsw.model.board.Building;
import it.polimi.ingsw.model.board.Coordinate;
import it.polimi.ingsw.view.client.ServerHandler;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;


public class GameViewModel {
    private final ServerHandler server;
    private final BoardViewModel boardViewModel;
    private final IntegerProperty turn = new SimpleIntegerProperty(0);
    private final ObjectProperty<User> currentUser = new SimpleObjectProperty<>();

    private Consumer<Boolean> onPlaceAttemptListener = null;
    private Consumer<Boolean> onActionAttemptListener = null;
    private Runnable onRequestPlaceListener = null;
    private Runnable onRequestWaitListener = null;
    private Consumer<List<ActionIdentifier>> onActionsReadyListener = null;

    private final List<Runnable> redrawListeners = new ArrayList<>();

    public void setOnActionsReadyListener(Consumer<List<ActionIdentifier>> actionsReadyListener) {
        this.onActionsReadyListener = actionsReadyListener;
    }

    public void setOnActionAttemptListener(Consumer<Boolean> onActionAttemptListener) {
        this.onActionAttemptListener = onActionAttemptListener;
    }

    public void addRedrawListener(Runnable r) {
        redrawListeners.add(r);
    }

    public void requestRedraw() {
        redrawListeners.forEach(Runnable::run);
    }

    public void setOnPlaceAttemptListener(Consumer<Boolean> onPlaceAttemptListener) {
        this.onPlaceAttemptListener = onPlaceAttemptListener;
    }

    public void setOnRequestPlaceListener(Runnable requestPlaceListener) {
        this.onRequestPlaceListener = requestPlaceListener;
    }

    public void setOnRequestWaitListener(Runnable requestWaitListener) {
        this.onRequestWaitListener = requestWaitListener;
    }

    public IntegerProperty turnProperty() {
        return turn;
    }

    public ObjectProperty<User> currentUserProperty() {
        return currentUser;
    }

    public BoardViewModel getBoardViewModel() {
        return boardViewModel;
    }

    public GameViewModel(ServerHandler server, BoardViewModel boardViewModel) {
        this.server = server;
        this.boardViewModel = boardViewModel;

        server.dispatcher().setOnTurnChangeListener(this::onTurnChange);
        server.dispatcher().setOnPawnPlacedListener(this::onPawnPlaced);
        server.dispatcher().setOnRequestPlacePawnsListener(this::onRequestPlace);
        server.dispatcher().setOnMoveListener(this::onMove);
        server.dispatcher().setOnBuildListener(this::onBuild);
        server.dispatcher().setOnEliminationListener(this::onElimination);
        server.dispatcher().setOnActionsReadyListener(this::onActionsReady);
    }

    public void placePawns(Coordinate c1, Coordinate c2) {
        server.dispatcher().setOnResultListener(r -> {
            if (onPlaceAttemptListener != null) onPlaceAttemptListener.accept(r);
            server.dispatcher().setOnResultListener(null);
        });
        server.onPlacePawns(boardViewModel.getMyUser().orElseThrow(), c1, c2);
    }

    public void executeAction(ActionIdentifier action, int pawnId, Coordinate target) {
        server.dispatcher().setOnResultListener(this::onExecuteResult);
        User myUser = boardViewModel.getMyUser().orElseThrow();
        server.onExecuteAction(myUser, pawnId, action, target);
    }

    private void onExecuteResult(boolean result) {
        if (onActionAttemptListener != null) {
            onActionAttemptListener.accept(result);
        }
    }

    private void onActionsReady(User user, List<ActionIdentifier> actionIdentifiers) {
        if (myUser(user)) {
            if (onActionsReadyListener != null) {
                onActionsReadyListener.accept(actionIdentifiers);
            }
        } else {
            if (onRequestWaitListener != null) {
                onRequestWaitListener.run();
            }
        }
    }

    private void onRequestPlace(User user) {
        if (onRequestPlaceListener != null && myUser(user)) {
            onRequestPlaceListener.run();
        }
    }

    private void onElimination(User user) {
        boardViewModel.getPlayer(user)
                .map(PlayerViewModel::getPawns)
                .ifPresent(l -> l.forEach(boardViewModel::removePawn));
        requestRedraw();
    }

    private void onBuild(Building building, Coordinate coordinate) {
        boardViewModel.build(building, coordinate);
        requestRedraw();
    }

    private void onMove(Coordinate c1, Coordinate c2) {
        boardViewModel.move(c1, c2);
        requestRedraw();
    }

    private void onPawnPlaced(User owner, int pawnId, Coordinate coordinate) {
        PlayerViewModel player = boardViewModel.getPlayer(owner).orElseThrow();
        PawnViewModel p = new PawnViewModel(player, pawnId);
        player.addPawn(p);
        boardViewModel.putPawn(p, coordinate);
        requestRedraw();
    }

    private void onTurnChange(User user, int i) {
        turn.set(i + 1);
        currentUser.setValue(user);
    }

    private boolean myUser(User user) {
        return boardViewModel.getMyUser()
                .map(user::equals)
                .orElse(false);
    }
}
