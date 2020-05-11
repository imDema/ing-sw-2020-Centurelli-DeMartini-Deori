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
    private final List<ServerEventsListener> serverEventsListeners = new ArrayList<>();
    private final GameCycle gameCycle = new GameCycle(lobby);

    public GameCycle getGameCycle() {
        return gameCycle;
    }

    public boolean isGameReady (){
        return lobby.isGameReady();
    }

    public void addServerEventsListener(ServerEventsListener serverEventsListener) {
        serverEventsListeners.add(serverEventsListener);
        gameCycle.addServerEventListener(serverEventsListener);
    }

    public void removeServerEventsListener(ServerEventsListener serverEventsListener) {
        serverEventsListeners.remove(serverEventsListener);
        gameCycle.removeServerEventsListener(serverEventsListener);
    }

    public void initLobby() {
        try {
            lobby.loadGods();
        } catch (IOException e) {
            serverEventsListeners.forEach(l -> l.onServerError("Input-Output error", "error while loading gods configuration"));
        }
        onGodsAvailable(lobby.getAvailableGods());
    }

    private void onGodsAvailable(List<God> gods) {
        if (lobby.isGameFull()) return;

        List<GodIdentifier> godsIds = gods.stream()
                .map(GodIdentifier::new).collect(Collectors.toList());
        serverEventsListeners.forEach(l -> l.onGodsAvailable(godsIds));
    }

    @Override
    public boolean onAddUser(User user) {
        synchronized (lobby) {
            if (lobby.addUser(user)) {
                serverEventsListeners.forEach(l -> l.onUserJoined(user));
                return true;
            } else {
                return false;
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
                serverEventsListeners.forEach(l -> l.onGodChosen(user, god));

                if (lobby.isGameFull()) {
                    serverEventsListeners.forEach(l -> l.onRequestPlacePawns(lobby.getUserToSetUp().orElseThrow()));
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
                    serverEventsListeners.forEach(l -> l.onRequestPlacePawns(nextUser.get()));
                } else {
                    gameCycle.startTurn();
                }
                return true;
            }
            return false;
        }
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
