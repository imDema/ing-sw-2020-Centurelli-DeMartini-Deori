package it.polimi.ingsw.view.client.gui;

import it.polimi.ingsw.controller.messages.User;
import it.polimi.ingsw.view.client.ServerHandler;
import it.polimi.ingsw.view.client.state.BoardViewModel;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;


public class GameViewModel {
    private final ServerHandler server;
    private final BoardViewModel boardViewModel;
    private final IntegerProperty turn = new SimpleIntegerProperty(0);
    private final ObjectProperty<User> currentUser = new SimpleObjectProperty<>();

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
    }

    private void onTurnChange(User user, int i) {
        turn.set(i + 1);
        currentUser.setValue(user);
    }
}
