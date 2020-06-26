package it.polimi.ingsw.view.client.controls;

import it.polimi.ingsw.controller.events.OnUserJoinedListener;
import it.polimi.ingsw.controller.messages.User;
import it.polimi.ingsw.view.client.ServerHandler;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Client side controller that handles the login phase
 */
public class LoginControl {
    private final ServerHandler server;
    private final BoardViewState boardViewState;

    private BiConsumer<Boolean, String> onLoginAttemptListener = null;
    private BiConsumer<Boolean, String> onSetSizeAttemptListener = null;
    private Consumer<Integer> onSizeSetListener = null;
    private OnUserJoinedListener onUserJoinedListener = null;

    public void setOnUserJoinedListener(OnUserJoinedListener onUserJoinedListener) {
        this.onUserJoinedListener = onUserJoinedListener;
    }

    public void setOnLoginAttempt(BiConsumer<Boolean, String> listener) {
        this.onLoginAttemptListener = listener;
    }

    public void setOnSetSizeAttempt(BiConsumer<Boolean, String> listener) {
        this.onSetSizeAttemptListener = listener;
    }

    public void setOnSizeSetListener(Consumer<Integer> onSizeSetListener) {
        this.onSizeSetListener = onSizeSetListener;
    }

    public LoginControl(ServerHandler server, BoardViewState boardViewState) {
        this.server = server;
        this.boardViewState = boardViewState;
        server.dispatcher().setOnUserJoinedListener(u -> {
            boardViewState.addPlayer(new PlayerViewState(u));
            if (onUserJoinedListener != null) {
                onUserJoinedListener.onUserJoined(u);
            }
        });
        server.dispatcher().setOnSizeSelectedListener(size -> {
            if (onSizeSetListener != null) {
                onSizeSetListener.accept(size);
            }
        });
    }

    /**
     * Request logging in with a username
     * @param username username to log in with
     */
    public void login(String username) {
        if (boardViewState.getMyUser().isEmpty()) {
            final User user = new User(username);
            server.dispatcher().setOnResultListener(r -> {
                if (r) {
                    boardViewState.setMyUser(user);
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

    /**
     * Request selecting the size of the game
     * @param size size of the game
     */
    public void setSize(int size) {
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
