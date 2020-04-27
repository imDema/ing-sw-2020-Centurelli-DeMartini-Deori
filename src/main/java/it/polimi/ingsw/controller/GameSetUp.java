package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.events.OnGodsAvailableListener;
import it.polimi.ingsw.controller.events.OnRequestPlacePawnsListener;
import it.polimi.ingsw.controller.events.OnServerErrorListener;
import it.polimi.ingsw.controller.messages.GodIdentifier;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Lobby;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.view.events.OnAddUserListener;
import it.polimi.ingsw.view.events.OnChooseGodListener;
import it.polimi.ingsw.model.player.God;
import it.polimi.ingsw.controller.messages.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class GameSetUp implements OnAddUserListener, OnChooseGodListener {

    private final int SIZE = 3;
    private Lobby lobby = new Lobby(SIZE);
    private List<God> gods;
    private OnGodsAvailableListener godsAvailableListener;
    private OnServerErrorListener serverErrorListener;
    private OnRequestPlacePawnsListener requestPlacePawnsListener;
    private List<User> users = new ArrayList<>();
    private Game game;

    public void setServerErrorListener(OnServerErrorListener serverErrorListener) {
        this.serverErrorListener = serverErrorListener;
    }

    public void setRequestPlacePawnsListener(OnRequestPlacePawnsListener requestPlacePawnsListener) {
        this.requestPlacePawnsListener = requestPlacePawnsListener;
    }

    public void setGodsAvailableListener(OnGodsAvailableListener godsAvailableListener) {
        this.godsAvailableListener = godsAvailableListener;
    }

    public void initLobby() {
        try {
            gods = Arrays.asList(lobby.getAvailableGods());
            onGodsAvailable(gods);
        } catch (IOException e) {
            if (serverErrorListener != null)
                serverErrorListener.onServerError("Input-Output error", "error while loading gods configuration");
        }

    }

    private void onGodsAvailable(List<God> gods) {
        List<GodIdentifier> godsIds = gods.stream()
                .map(GodIdentifier::new).collect(Collectors.toList());
        if (godsAvailableListener != null)
            godsAvailableListener.onGodsAvailable(godsIds);
    }

    @Override
    public Optional<User> onAddUser(String username) {
        User user = new User(username);
        if (!users.contains(user) && users.size() < SIZE ) {
            users.add(user);
            return Optional.of(user);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public boolean onChooseGod(User user, GodIdentifier god) {
        Optional<God> g = gods.stream()
                .filter(god::matches)
                .findFirst();
        if (users.contains(user) && g.isPresent() && !lobby.isFull()) {
            Player player = new Player(user,g.get());
            if (lobby.addPlayer(player)) {
                gods.remove(g.get());
                onGodsAvailable(gods);
                if (lobby.isFull()) {
                    game = lobby.createGame();
                }
                return true;
            }
        }
        return false;
    }
}
