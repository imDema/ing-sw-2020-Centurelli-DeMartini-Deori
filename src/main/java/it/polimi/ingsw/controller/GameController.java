package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.events.OnGameFinishedListener;
import it.polimi.ingsw.controller.events.OnServerErrorListener;
import it.polimi.ingsw.controller.events.OnServerEventListener;
import it.polimi.ingsw.controller.messages.ActionIdentifier;
import it.polimi.ingsw.controller.messages.GodIdentifier;
import it.polimi.ingsw.controller.messages.User;
import it.polimi.ingsw.model.Lobby;
import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.board.Coordinate;
import it.polimi.ingsw.model.player.God;
import it.polimi.ingsw.view.events.OnClientEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class GameController implements OnClientEventListener, OnServerErrorListener {
    private final Lobby lobby = new Lobby();
    private final GameCycle gameCycle = new GameCycle(lobby, this);
    private final List<OnServerEventListener> serverEventListeners = new ArrayList<>();
    private OnGameFinishedListener gameFinishedListener = null;

    // Method for testing
    protected Board getBoard() {
        return lobby.getGame().getBoard();
    }

    public boolean isGameReady (){
        return lobby.getSize() > 0 && lobby.isGameReady();
    }
    public boolean isLobbyFull (){
        return lobby.getSize() > 0 && lobby.isLobbyFull();
    }

    public void addServerEventsListener(OnServerEventListener onServerEventListener) {
        serverEventListeners.add(onServerEventListener);
        gameCycle.addServerEventListener(onServerEventListener);

        // Notify of lobby state
        if (lobby.getSize() != 0) {
            onServerEventListener.onSizeSelected(lobby.getSize());
        }
        lobby.getUsers().forEach(onServerEventListener::onUserJoined);
    }

    public void removeServerEventsListener(OnServerEventListener onServerEventListener) {
        serverEventListeners.remove(onServerEventListener);
        gameCycle.removeServerEventsListener(onServerEventListener);
        if (serverEventListeners.size() == 0 && gameFinishedListener != null) {
            gameFinishedListener.onGameFinished(this);
            gameFinishedListener = null; // Prevent re triggering game finish
        }
    }

    public void setGameFinishedListener(OnGameFinishedListener gameFinishedListener) {
        this.gameFinishedListener = gameFinishedListener;
        gameCycle.setGameFinishedListener(gameFinishedListener);
    }

    private void onGodsAvailable(List<God> gods) {
        List<GodIdentifier> godsIds = gods.stream()
                .map(GodIdentifier::new).collect(Collectors.toList());
        serverEventListeners.forEach(l -> l.onGodsAvailable(godsIds));
    }

    @Override
    public boolean onAddUser(User user) {
        synchronized (lobby) {
            if (lobby.getSize() == 0)
                return false;

            if (lobby.addUser(user)) {
                serverEventListeners.forEach(l -> l.onUserJoined(user));
                if (lobby.isLobbyFull()) {
                    List<God> gods = lobby.getAllGods();
                    if (gods == null || gods.size() < lobby.getSize()) {
                        onServerError("Error retrieving gods", "An error occurred while loading God data from the configuration files");
                        return false;
                    }
                    onGodsAvailable(gods);
                }
                return true;
            } else {
                return false;
            }
        }
    }

    @Override
    public boolean onChooseGod(User user, GodIdentifier god) {
        synchronized (lobby) {
            if (lobby.getSize() == 0)
                return false;

            Optional<User> challenger = lobby.getChallenger();
            if (challenger.isEmpty() || lobby.isGameFull() || (user.equals(challenger.get()) && lobby.getSize() > 1) ||
                lobby.getPlayer(user).isPresent())
                return false;

            Optional<God> g = lobby.getAvailableGods().stream()
                    .filter(god::matches)
                    .findFirst();
            if (g.isPresent() && lobby.chooseGod(user, g.get())) {
                serverEventListeners.forEach(l -> l.onGodChosen(user, god));

                if (lobby.getPlayerNumber() == lobby.getSize() - 1) {
                    God lastGod = lobby.getAvailableGods().get(0);
                    if (!lobby.chooseGod(challenger.get(), lastGod)) {
                        onServerError("Error assigning last god to challenger", "Could not assign the last god to the challenger");
                        return false;
                    }
                    serverEventListeners.forEach(l -> l.onGodChosen(challenger.get(), new GodIdentifier(lastGod)));
                }
                onGodsAvailable(lobby.getAvailableGods());
                return true;
            }
            return false;
        }
    }

    @Override
    public boolean onPlacePawns(User user, Coordinate c1, Coordinate c2) {
        synchronized (lobby) {
            if (lobby.getSize() == 0)
                return false;

            if (!lobby.isGameFull() || !lobby.choseFirstPlayer() || !(lobby.getUserToSetUp().map(user::equals).orElse(false)))
                return false;

            if (lobby.setUpUserPawns(user, c1, c2)) {
                serverEventListeners.forEach(l -> l.onPawnPlaced(user, 0, c1));
                serverEventListeners.forEach(l -> l.onPawnPlaced(user, 1, c2));
                Optional<User> nextUser = lobby.getUserToSetUp();
                if (nextUser.isPresent()) {
                    serverEventListeners.forEach(l -> l.onRequestPlacePawns(nextUser.get()));
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
        synchronized (lobby) {
            if (lobby.getSize() == 0 && size >= 1 && size <= 3) {
                lobby.setSize(size);
                serverEventListeners.forEach(l -> l.onSizeSelected(size));
                return true;
            } else {
                return false;
            }
        }
    }

    private List<God> getCorrespondingGods(List<GodIdentifier> selectedGods) {
        List<God> gods = new ArrayList<>();
        for (GodIdentifier id : selectedGods) {
            lobby.getAllGods().stream()
                    .filter(g -> id.getName().equals(g.getName()))
                    .findAny()
                    .ifPresent(gods::add);
        }
        return gods;
    }

    @Override
    public boolean onSelectGods(User user, List<GodIdentifier> selectedGods) {
        synchronized (lobby) {
            if (isLobbyFull() && lobby.getChallenger().isEmpty()) {
                List<God> gods = getCorrespondingGods(selectedGods);
                if (gods.size() == selectedGods.size() && gods.size() == lobby.getSize() && lobby.setChallenger(user)) {
                    lobby.setAvailableGods(gods);
                    serverEventListeners.forEach(l -> l.onGodsAvailable(selectedGods));
                    return true;
                }
            }
            return false;
        }
    }

    @Override
    public boolean onChooseFirstPlayer(User self, User firstPlayer) {
        synchronized (lobby) {
            if (!lobby.isGameFull() || lobby.choseFirstPlayer() ||
                    !lobby.getChallenger().map(self::equals).orElse(false) ||
                    (self.equals(firstPlayer) && lobby.getSize() > 1)) {
                return false;
            }
            if (lobby.setFirstUser(firstPlayer)) {
                serverEventListeners.forEach(l -> l.onRequestPlacePawns(lobby.getUserToSetUp().orElseThrow()));
                return true;
            } else {
                return false;
            }
        }
    }

    // Broadcast fatal error
    @Override
    public void onServerError(String type, String description) {
        serverEventListeners.forEach(l -> l.onServerError(type, description));
        if (gameFinishedListener != null) {
            gameFinishedListener.onGameFinished(this);
            gameFinishedListener = null; // Prevent re triggering game finish
        }
    }

}
