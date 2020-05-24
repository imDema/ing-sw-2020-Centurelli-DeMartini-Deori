package it.polimi.ingsw.view.client.gui.game;

import it.polimi.ingsw.controller.messages.User;
import it.polimi.ingsw.model.board.Coordinate;
import it.polimi.ingsw.view.client.ServerHandler;
import it.polimi.ingsw.view.client.state.BoardViewModel;
import it.polimi.ingsw.view.client.state.PawnViewModel;
import it.polimi.ingsw.view.client.state.PlayerViewModel;
import javafx.application.Platform;
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

    //TODO remove this, just for testing
    private List<Runnable> redrawListeners = new ArrayList<>();

    public void addRedrawListener(Runnable r) {
        redrawListeners.add(r);
    }

    public void requestRedraw() {
        Platform.runLater(() -> redrawListeners.forEach(Runnable::run));
    }

    private Consumer<Boolean> onPlaceAttemptListener = null;
    private Consumer<User> requestPlaceListener = null;

    public void setOnPlaceAttemptListener(Consumer<Boolean> onPlaceAttemptListener) {
        this.onPlaceAttemptListener = onPlaceAttemptListener;
    }

    public void setOnRequestPlaceListener(Consumer<User> requestPlaceListener) {
        server.dispatcher().setOnRequestPlacePawnsListener(requestPlaceListener::accept);
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
    }

    public void placePawns(Coordinate c1, Coordinate c2) {
        server.dispatcher().setOnResultListener(r -> {
            if (onPlaceAttemptListener != null) onPlaceAttemptListener.accept(r);
            server.dispatcher().setOnResultListener(null);
        });
        server.onPlacePawns(boardViewModel.getMyUser().orElseThrow(), c1, c2);
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
}
