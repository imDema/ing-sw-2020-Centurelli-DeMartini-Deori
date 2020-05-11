package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.events.*;
import it.polimi.ingsw.controller.messages.ActionIdentifier;
import it.polimi.ingsw.controller.messages.GodIdentifier;
import it.polimi.ingsw.model.Lobby;
import it.polimi.ingsw.model.board.Coordinate;
import it.polimi.ingsw.view.events.*;
import it.polimi.ingsw.model.player.God;
import it.polimi.ingsw.controller.messages.User;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class GameController implements ClientEventsListener {

    private final int SIZE = 3;
    private final Lobby lobby = new Lobby(SIZE);
    private OnGodsAvailableListener godsAvailableListener;
    private OnGodChosenListener godChosenListener;
    private OnServerErrorListener serverErrorListener;
    private OnUserJoinedListener userJoinedListener;
    private OnRequestPlacePawnsListener requestPlacePawnsListener;
    private final GameCycle gameCycle = new GameCycle(lobby);

    public GameCycle getGameCycle() {
        return gameCycle;
    }

    public void setServerEventsListener(ServerEventsListener serverEventsListener) {
        godsAvailableListener = serverEventsListener;
        godChosenListener = serverEventsListener;
        serverErrorListener = serverEventsListener;
        userJoinedListener = serverEventsListener;
        requestPlacePawnsListener = serverEventsListener;
        gameCycle.setServerEventListener(serverEventsListener);
    }

    public void setGodChosenListener(OnGodChosenListener godChosenListener) {
        this.godChosenListener = godChosenListener;
    }

    public void setServerErrorListener(OnServerErrorListener serverErrorListener) {
        this.serverErrorListener = serverErrorListener;
    }

    public void setUserJoinedListener(OnUserJoinedListener userJoinedListener) {
        this.userJoinedListener = userJoinedListener;
    }

    public void setRequestPlacePawnsListener(OnRequestPlacePawnsListener requestPlacePawnsListener) {
        this.requestPlacePawnsListener = requestPlacePawnsListener;
    }

    public void setGodsAvailableListener(OnGodsAvailableListener godsAvailableListener) {
        this.godsAvailableListener = godsAvailableListener;
    }

    public void initLobby() {
        try {
            lobby.loadGods();
        } catch (IOException e) {
            if (serverErrorListener != null)
                serverErrorListener.onServerError("Input-Output error", "error while loading gods configuration");
        }
        onGodsAvailable(lobby.getAvailableGods());
    }

    private void onGodsAvailable(List<God> gods) {
        if (lobby.isGameFull()) return;

        List<GodIdentifier> godsIds = gods.stream()
                .map(GodIdentifier::new).collect(Collectors.toList());
        if (godsAvailableListener != null)
            godsAvailableListener.onGodsAvailable(godsIds);
    }

    private void onRequestPlacement(User user) {
        if (requestPlacePawnsListener != null)
            requestPlacePawnsListener.onRequestPlacePawns(user);
    }

    @Override
    public Optional<User> onAddUser(String username) {
        synchronized (lobby) {
            User user = new User(username);
            if (lobby.addUser(user)) {
                if (userJoinedListener != null) {
                    userJoinedListener.onUserJoined(user);
                }
                return Optional.of(user);
            } else {
                return Optional.empty();
            }
        }
    }

    @Override
    public boolean onChooseGod(User user, GodIdentifier god) {
        synchronized (lobby) {
            if (lobby.isGameFull()) return false;

            Optional<God> g = lobby.getAvailableGods().stream()
                    .filter(god::matches)
                    .findFirst();
            if (g.isPresent() && lobby.chooseGod(user, g.get())) {
                if (godChosenListener != null) {
                    godChosenListener.onGodChosen(user, god);
                }

                if (lobby.isGameFull()) {
                    onRequestPlacement(lobby.getUserToSetUp().orElseThrow());
                } else {
                    onGodsAvailable(lobby.getAvailableGods());
                }
                return true;
            }
            return false;
        }
    }

    @Override
    public boolean onPlacePawns(User user, Coordinate c1, Coordinate c2) {
        synchronized (lobby) {
            Optional<User> u = lobby.getUserToSetUp();
            if (!lobby.isGameFull() || !(u.isPresent() && u.get().equals(user)))
                return false;

            if (lobby.setUpUserPawns(user, c1, c2)) {
                Optional<User> nextUser = lobby.getUserToSetUp();
                if (nextUser.isPresent()) {
                    onRequestPlacement(nextUser.get());
                } else {
                    gameCycle.startTurn();
                }
                return true;
            }
            return false;
        }
    }

    public boolean isGameReady (){
        return lobby.isGameReady();
    }

    @Override
    public boolean onCheckAction(User user, int pawnId, ActionIdentifier actionIdentifier, Coordinate coordinate) {
        if (isGameReady()) {
            return gameCycle.onCheckAction(user, pawnId, actionIdentifier, coordinate);
        } else {
            return false;
        }
    }

    @Override
    public boolean onExecuteAction(User user, int pawnId, ActionIdentifier actionIdentifier, Coordinate coordinate) {
        if (isGameReady()) {
            return gameCycle.onExecuteAction(user, pawnId, actionIdentifier, coordinate);
        } else {
            return false;
        }
    }
}
