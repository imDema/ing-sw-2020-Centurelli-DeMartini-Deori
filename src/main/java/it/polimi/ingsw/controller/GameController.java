package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.events.OnGameFinishedListener;
import it.polimi.ingsw.controller.events.OnServerErrorListener;
import it.polimi.ingsw.controller.events.ServerEventsListener;
import it.polimi.ingsw.controller.messages.ActionIdentifier;
import it.polimi.ingsw.controller.messages.GodIdentifier;
import it.polimi.ingsw.controller.messages.User;
import it.polimi.ingsw.model.Lobby;
import it.polimi.ingsw.model.board.Coordinate;
import it.polimi.ingsw.model.player.God;
import it.polimi.ingsw.view.events.ClientEventsListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class GameController implements ClientEventsListener, OnServerErrorListener {
    private final Lobby lobby = new Lobby();
    private final GameCycle gameCycle = new GameCycle(lobby, this);
    private final List<ServerEventsListener> serverEventsListeners = new ArrayList<>();
    private OnGameFinishedListener gameFinishedListener = null;

    public boolean isGameReady (){
        return lobby.getSize() > 0 && lobby.isGameReady();
    }
    public boolean isLobbyFull (){
        return lobby.getSize() > 0 && lobby.isLobbyFull();
    }

    public void addServerEventsListener(ServerEventsListener serverEventsListener) {
        serverEventsListeners.add(serverEventsListener);
        gameCycle.addServerEventListener(serverEventsListener);
    }

    public void removeServerEventsListener(ServerEventsListener serverEventsListener) {
        serverEventsListeners.remove(serverEventsListener);
        gameCycle.removeServerEventsListener(serverEventsListener);
        if (serverEventsListeners.size() == 0 && gameFinishedListener != null) {
            gameFinishedListener.onGameFinished(this);
            gameFinishedListener = null; // Prevent re triggering game finish
        }
    }

    public void setGameFinishedListener(OnGameFinishedListener gameFinishedListener) {
        this.gameFinishedListener = gameFinishedListener;
        gameCycle.setGameFinishedListener(gameFinishedListener);
    }

    private void onGodsAvailable(List<God> gods) {
        if (lobby.isGameFull()) return;

        List<GodIdentifier> godsIds = gods.stream()
                .map(GodIdentifier::new).collect(Collectors.toList());
        serverEventsListeners.forEach(l -> l.onGodsAvailable(godsIds));
    }

    @Override
    public boolean onAddUser(User user) {
        if (lobby.getSize() == 0)
            return false;

        synchronized (lobby) {
            if (lobby.addUser(user)) {
                serverEventsListeners.forEach(l -> l.onUserJoined(user));
                if (lobby.isLobbyFull())
                    onGodsAvailable(lobby.getAvailableGods());
                return true;
            } else {
                return false;
            }
        }
    }

    @Override
    public boolean onChooseGod(User user, GodIdentifier god) {
        if (lobby.getSize() == 0)
            return false;

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
        if (lobby.getSize() == 0)
            return false;

        synchronized (lobby) {
            Optional<User> u = lobby.getUserToSetUp();
            if (!lobby.isGameFull() || !(u.isPresent() && u.get().equals(user)))
                return false;

            if (lobby.setUpUserPawns(user, c1, c2)) {
                serverEventsListeners.forEach(l -> l.onPawnPlaced(user, 0, c1));
                serverEventsListeners.forEach(l -> l.onPawnPlaced(user, 1, c2));
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

    @Override
    public boolean onSelectPlayerNumber(int size) {
        if (lobby.getSize() == 0 && size >= 1 && size <=3) {
            lobby.setSize(size);

            lobby.loadGods();
            if (lobby.getAvailableGods() == null || lobby.getAvailableGods().size() < lobby.getSize()) {
                serverEventsListeners.forEach(l -> l.onServerError("Error retrieving gods", "An error occurred while loading God data from the configuration files"));
            }
            return true;
        } else {
            return false;
        }
    }

    // Broadcast fatal error
    @Override
    public void onServerError(String type, String description) {
        serverEventsListeners.forEach(l -> l.onServerError(type, description));
        if (gameFinishedListener != null) {
            gameFinishedListener.onGameFinished(this);
            gameFinishedListener = null; // Prevent re triggering game finish
        }
    }
}
