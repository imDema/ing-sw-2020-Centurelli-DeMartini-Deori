package it.polimi.ingsw.view.client.controls;

import it.polimi.ingsw.controller.messages.ActionIdentifier;
import it.polimi.ingsw.controller.messages.User;
import it.polimi.ingsw.model.board.Building;
import it.polimi.ingsw.model.board.Coordinate;
import it.polimi.ingsw.view.client.ServerHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Client side controller that handles the main part of the game cycle
 */
public class GameControl {
    private final ServerHandler server;
    private final BoardViewState boardViewState;
    private Consumer<Boolean> onPlaceAttemptListener = null;
    private Consumer<Boolean> onActionAttemptListener = null;
    private Runnable onRequestPlaceListener = null;
    private Runnable onRequestWaitListener = null;
    private Consumer<List<ActionIdentifier>> onActionsReadyListener = null;
    private BiConsumer<Integer, User> onTurnChangeListener = null;
    private final List<Runnable> redrawListeners = new ArrayList<>();

    public GameControl(ServerHandler server, BoardViewState boardViewState) {
        this.server = server;
        this.boardViewState = boardViewState;

        server.dispatcher().setOnTurnChangeListener(this::onTurnChange);
        server.dispatcher().setOnPawnPlacedListener(this::onPawnPlaced);
        server.dispatcher().setOnRequestPlacePawnsListener(this::onRequestPlace);
        server.dispatcher().setOnMoveListener(this::onMove);
        server.dispatcher().setOnBuildListener(this::onBuild);
        server.dispatcher().setOnEliminationListener(this::onElimination);
        server.dispatcher().setOnActionsReadyListener(this::onActionsReady);
    }

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

    public void setOnTurnChangeListener(BiConsumer<Integer, User> onTurnChangeListener) {
        this.onTurnChangeListener = onTurnChangeListener;
    }

    public BoardViewState getBoardViewState() {
        return boardViewState;
    }

    /**
     * Request pawn placement
     * @param c1 coordinate of the first pawn
     * @param c2 coordinate of the second pawn
     */
    public void placePawns(Coordinate c1, Coordinate c2) {
        server.dispatcher().setOnResultListener(r -> {
            if (onPlaceAttemptListener != null) onPlaceAttemptListener.accept(r);
            server.dispatcher().setOnResultListener(null);
        });
        server.onPlacePawns(boardViewState.getMyUser().orElseThrow(), c1, c2);
    }

    /**
     * Request action execution
     * @param action action to execute
     * @param pawnId pawn to execute the action with
     * @param target target coordinate for the action
     */
    public void executeAction(ActionIdentifier action, int pawnId, Coordinate target) {
        server.dispatcher().setOnResultListener(this::onExecuteResult);
        User myUser = boardViewState.getMyUser().orElseThrow();
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
        boardViewState.getPlayer(user)
                .map(PlayerViewState::getPawns)
                .ifPresent(l -> l.forEach(boardViewState::removePawn));
        requestRedraw();
    }

    private void onBuild(Building building, Coordinate coordinate) {
        boardViewState.build(building, coordinate);
        requestRedraw();
    }

    private void onMove(Coordinate c1, Coordinate c2) {
        boardViewState.move(c1, c2);
        requestRedraw();
    }

    private void onPawnPlaced(User owner, int pawnId, Coordinate coordinate) {
        PlayerViewState player = boardViewState.getPlayer(owner).orElseThrow();
        PawnViewState p = new PawnViewState(player, pawnId);
        player.addPawn(p);
        boardViewState.putPawn(p, coordinate);
        requestRedraw();
    }

    private void onTurnChange(User user, int i) {
        if(onTurnChangeListener != null)
            onTurnChangeListener.accept(i + 1, user);
    }

    private boolean myUser(User user) {
        return boardViewState.getMyUser()
                .map(user::equals)
                .orElse(false);
    }
}
