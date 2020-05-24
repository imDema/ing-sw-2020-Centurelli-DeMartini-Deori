package it.polimi.ingsw.view.client.gui.setup;

import it.polimi.ingsw.controller.events.OnUserJoinedListener;
import it.polimi.ingsw.controller.messages.User;
import it.polimi.ingsw.view.client.ServerHandler;
import it.polimi.ingsw.view.client.state.BoardViewModel;
import it.polimi.ingsw.view.client.state.PlayerViewModel;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.function.BiConsumer;

public class LoginViewModel {
    private final ServerHandler server;
    private final BoardViewModel boardViewModel;

    private BiConsumer<Boolean, String> onLoginAttemptListener = null;
    private BiConsumer<Boolean, String> onSetSizeAttemptListener = null;
    private OnUserJoinedListener onUserJoinedListener = null;

    private final StringProperty username = new SimpleStringProperty("");
    private final IntegerProperty size = new SimpleIntegerProperty(3);

    public StringProperty usernameProperty() {
        return username;
    }

    public IntegerProperty sizeProperty() {
        return size;
    }

    public void setOnUserJoinedListener(OnUserJoinedListener onUserJoinedListener) {
        this.onUserJoinedListener = onUserJoinedListener;
    }

    public void setOnLoginAttempt(BiConsumer<Boolean, String> listener) {
        this.onLoginAttemptListener = listener;
    }

    public void setOnSetSizeAttempt(BiConsumer<Boolean, String> listener) {
        this.onSetSizeAttemptListener = listener;
    }

    public LoginViewModel(ServerHandler server, BoardViewModel boardViewModel) {
        this.server = server;
        this.boardViewModel = boardViewModel;
        server.dispatcher().setOnUserJoinedListener(u -> {
            boardViewModel.addPlayer(new PlayerViewModel(u));
            if (onUserJoinedListener != null) {
                onUserJoinedListener.onUserJoined(u);
            }
        });
    }

    public void login() {
        if (boardViewModel.getMyUser().isEmpty()) {
            final User user = new User(usernameProperty().get());
            server.dispatcher().setOnResultListener(r -> {
                if (r) {
                    boardViewModel.setMyUser(user);
                    onLoginAttemptListener.accept(true, "Successfully logged in");
                } else {
                    onLoginAttemptListener.accept(false, "");
                }
            });
            server.onAddUser(user);
        } else {
            onLoginAttemptListener.accept(false, "You are already logged in");
        }
    }

    public void setSize() {
        final int size = sizeProperty().get();
        server.dispatcher().setOnResultListener(r -> {
            if (r) {
                onSetSizeAttemptListener.accept(true, "Successfully set player number");
            } else {
                if (size >= 2 && size <= 3) {
                    onSetSizeAttemptListener.accept(false, "Player number was already set");
                } else {
                    onSetSizeAttemptListener.accept(false, "Invalid player number");
                }
            }
        });
        server.onSelectPlayerNumber(size);
    }
}
