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
import it.polimi.ingsw.model.board.events.OnBuildListener;
import it.polimi.ingsw.model.board.events.OnMoveListener;
import it.polimi.ingsw.model.player.God;
import it.polimi.ingsw.view.events.OnClientEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Main controller class, handles the lobby and game setup, then forwards to a {@link GameCycle}
 */
public class GameController implements OnClientEventListener, OnServerErrorListener {
    private final Long uid = new Random().nextLong();

    private final Lobby lobby = new Lobby();
    private final GameCycle gameCycle = new GameCycle(lobby, this);
    private final List<OnServerEventListener> serverEventListeners = new ArrayList<>();
    private OnGameFinishedListener gameFinishedListener = null;

    // For testing
    protected Board getBoard() {
        return lobby.getGame().getBoard();
    }

    /**
     * Get a (statistically) unique id. Random long generated on creation.
     * Used for logging
     * @return id for this controller
     */
    public long getUid() {
        return uid;
    }

    /**
     * Get a short string representation of part of the uid.
     * Used for logging
     * Example: "[a3b4c5]".
     * @return hexadecimal representation of the last bits of the uid
     */
    public String getUidShortString() {
        return "[" + Long.toHexString(uid & 0xFFFFFF) + "]";
    }

    /**
     * Returns true if all players have chosen a god and placed their pawns
     * @return true if all players have chosen a god and placed their pawns
     */
    public boolean isGameReady (){
        return lobby.getSize() > 0 && lobby.isGameReady();
    }

    /**
     * Returns true if the lobby contains the maximum number of players
     * @return true if the lobby contains the maximum number of players
     */
    public boolean isLobbyFull (){
        return lobby.getSize() > 0 && lobby.isLobbyFull();
    }

    /**
     * Add a {@link OnServerEventListener} that will be notified with changes to the model and requests
     * @param onServerEventListener listener
     */
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
        if (serverEventListeners.size() == 0) {
            onGameFinished();
        }
    }

    /**
     * Add a listener that will be notified when the game is finished
     * @param gameFinishedListener listener
     */
    public void setGameFinishedListener(OnGameFinishedListener gameFinishedListener) {
        this.gameFinishedListener = gameFinishedListener;
    }

    private void onGodsAvailable(List<God> gods) {
        List<GodIdentifier> godsIds = gods.stream()
                .map(GodIdentifier::new).collect(Collectors.toList());
        serverEventListeners.forEach(l -> l.onGodsAvailable(godsIds));
    }

    /**
     * Add a new user
     * @param user User to add
     * @return true if the user is successfully added to the lobby, false otherwise
     */
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

    /**
     * Assign a god to an already logged in user
     * @return true if the god was successfully assigned, false otherwise
     */
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

                if (lobby.getGame().getPlayerNumber() == lobby.getSize() - 1) {
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

    /**
     * Place pawns for a user at the chosen coordinates
     * @return true if the pawns are placed successfully, false otherwise
     */
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

    /**
     * Check if an user can execute an action without violating the game rules
     * @param user user that wants to execute the action
     * @param actionIdentifier identifier for the action that should be checked
     * @param coordinate target coordinate for the action
     * @return true if the action is valid, false otherwise
     */
    @Override
    public boolean onCheckAction(User user, int pawnId, ActionIdentifier actionIdentifier, Coordinate coordinate) {
        if (isGameReady()) {
            return gameCycle.onCheckAction(user, pawnId, actionIdentifier, coordinate);
        } else {
            return false;
        }
    }

    /**
     * Check if an action is allowed and execute it. The registered {@link OnServerEventListener} will be notified
     * of changes to the model, winning and elimination. After executing an action successfully the current user will
     * be notified of his next allowed actions if his turn isn't over, otherwise the turn will advance and the next
     * user will receive his {@link ActionIdentifier} list.
     * @param user user that wants to execute the action
     * @param pawnId id of the pawn that should execute the action
     * @param actionIdentifier The ActionIdentifier corresponding to the chosen action
     * @param coordinate target coordinate for the action
     * @return true if the action was executed successfully
     * @see OnServerEventListener
     * @see it.polimi.ingsw.controller.events.OnWinListener
     * @see it.polimi.ingsw.controller.events.OnEliminationListener
     * @see OnMoveListener
     * @see OnBuildListener
     */
    @Override
    public boolean onExecuteAction(User user, int pawnId, ActionIdentifier actionIdentifier, Coordinate coordinate) {
        if (isGameReady()) {
            return gameCycle.onExecuteAction(user, pawnId, actionIdentifier, coordinate);
        } else {
            return false;
        }
    }

    /**
     * Select the number of players for this game
     * @return true if the size is valid and not already set
     */
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

    /**
     * Select which gods the other players will be able to pick from and assume the role of challenger
     * @param selectedGods list of gods that will be available for this game
     * @return true if successful, false otherwise
     */
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

    /**
     * Select which of the other players will be the first to start.
     * Only allowed if the user requesting is the challenger
     * @return true if successful, false otherwise
     */
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

    protected void onGameFinished() {
        if (gameFinishedListener != null) {
            gameFinishedListener.onGameFinished(this);
            gameFinishedListener = null; // Prevent re triggering game finish
        }
    }

    /**
     * Notify all listeners of a server error and signal the termination of the game
     */
    @Override
    public void onServerError(String type, String description) {
        serverEventListeners.forEach(l -> l.onServerError(type, description));
        onGameFinished();
    }

}
