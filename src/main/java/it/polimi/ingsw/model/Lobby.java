package it.polimi.ingsw.model;

import com.google.gson.JsonSyntaxException;
import it.polimi.ingsw.Resources;
import it.polimi.ingsw.controller.messages.User;
import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.board.Coordinate;
import it.polimi.ingsw.model.board.InvalidActionException;
import it.polimi.ingsw.model.player.God;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.turn.CircularList;
import it.polimi.ingsw.serialization.Serializer;
import it.polimi.ingsw.view.cli.CLI;

import java.util.*;

/**
 * Lobby for a game of Santorini. Handles the joining of users and the setup of the game board.
 * @see Board
 * @see User
 */
public class Lobby {
    public final int PAWN_N = 2;
    public final int MAX_NAME_LENGTH = 16;

    private final Game game = new Game();
    private int size = 0;
    private final List<User> users = new ArrayList<>();
    private final Map<User, Player> userPlayerMap = new HashMap<>();
    private final Map<Player, User> playerUserMap = new HashMap<>();
    private int readyUsers = 0;
    private boolean choseFirstPlayer = false;
    private User challenger = null;
    private List<God> allGods = null;
    private List<God> availableGods = null;

    public Game getGame() {
        return game;
    }

    /**
     * Choose which of the users will be the challenger for this match.
     * @param challenger User that should become the challenger
     * @return true if the user was in the lobby and the challenger was successfully set, false otherwise
     */
    public boolean setChallenger(User challenger) {
        if(users.contains(challenger)) {
            this.challenger = challenger;
            return true;
        }
        return false;
    }

    /**
     * Set which user will be the first to place his pawns and start moving after the pawn placing phase.
     * @param user user to set as starting player
     * @return true if the first user was successfully set, false if it was not in the lobby
     */
    public boolean setFirstUser(User user) {
        CircularList<Player> playerTurnList = game.getPlayerTurnList();
        for(int i = 0; i < size; i++) {
            if (user.matches(playerTurnList.current())) {
                choseFirstPlayer = true;
                return true;
            } else {
                playerTurnList.next();
            }
        }
        return false;
    }

    /**
     * @return true if the first user has been chosen
     */
    public boolean choseFirstPlayer() {
        return choseFirstPlayer;
    }

    /**
     * @return The challenger if it was chosen, {@code Optional.empty()} otherwise
     */
    public Optional<User> getChallenger() {
        return Optional.ofNullable(challenger);
    }

    /**
     * Get the list of users that joined the lobby
     * @return List of users in the lobby
     */
    public List<User> getUsers() {
        return users;
    }

    /**
     * Get the user corresponding to the supplied player
     * @param player player
     * @return The user if it exists, Optional.empty() if no matching user was found
     */
    public Optional<User> getUser(Player player) {
        return Optional.ofNullable(playerUserMap.get(player));
    }

    /**
     * Get the player corresponding to the supplied user
     * @param user user
     * @return The player if it exists, Optional.empty() if no matching player was found
     */
    public Optional<Player> getPlayer(User user) {
        return Optional.ofNullable(userPlayerMap.get(user));
    }

    /**
     * Add a {@link User} to the lobby. Does not allow duplicate usernames, empty usernames or usernames
     * longer than {@code MAX_NAME_LENGTH}
     * @param user user to add to the lobby
     * @return true if the user was added successfully, false otherwise
     */
    public boolean addUser(User user) {
        if (!users.contains(user) && users.size() < size &&
                user.getUsername().length() > 0 && user.getUsername().length() < MAX_NAME_LENGTH) {
            users.add(user);
            return true;
        }
        return false;
    }

    /**
     * Get the user that should place his pawns at this moment.
     * @return the user if a user has to be set up at this moment, Optional.empty() otherwise
     */
    public Optional<User> getUserToSetUp() {
        if (readyUsers < size) {
            Player p = game.getPlayerTurnList().current();
            User user = playerUserMap.get(p);
            if (p == null) {
                throw new IllegalStateException();
            }
            return Optional.of(user);
        } else {
            return Optional.empty();
        }
    }

    /**
     * Set the starting position for a user's pawns. Note: this method will check {@code getUserToSetUp()} and
     * only allow users to set up their pawn if it's the right moment
     * @param user to set up
     * @param c1 position of the first pawn
     * @param c2 position of the second pawn
     * @return true if the pawns are placed successfully
     */
    public boolean setUpUserPawns (User user, Coordinate c1, Coordinate c2) {
        if (!getUserToSetUp().map(user::equals).orElse(false))
            return false;


        Player player = userPlayerMap.get(user);
        Board board = game.getBoard();
        try {
            board.putPawn(player.getPawn(0), c1);
        } catch (InvalidActionException e) {
            return false;
        }
        try {
            board.putPawn(player.getPawn(1), c2);
        } catch (InvalidActionException e) {
            board.removePawn(player.getPawn(0));
            return false;
        }
        game.getPlayerTurnList().next();
        readyUsers += 1;

        return true;
    }

    /**
     * Choose a god card for a user. The user must be in the lobby and it must not have already chosen a god
     * @param user user that is choosing the {@link God}
     * @param god god card for the {@link User}
     * @return true if the god was chosen successfully, false otherwise
     */
    public boolean chooseGod(User user, God god) {
        if (users.contains(user) && availableGods.contains(god)) {
            Player player = new Player(user, god);
            game.addPlayer(player);
            availableGods.remove(god);
            userPlayerMap.put(user, player);
            playerUserMap.put(player, user);
            return true;
        }
        return false;
    }

    /**
     * Get the size of the {@link Lobby} (and the {@link Game}). The size is intended as number of players for the match
     * @return Selected size of the lobby
     */
    public int getSize() {
        return size;
    }

    /**
     * Sets the size of the lobby, allowed only once
     * @param size Size of the lobby
     */
    public void setSize(int size) {
        if (this.size == 0) {
            this.size = size;
        }
    }

    /**
     * Set the list of {@link God} the players will be allowed to pick from
     * @param availableGods list of allowed gods
     */
    public void setAvailableGods(List<God> availableGods) {
        this.availableGods = availableGods;
    }

    /**
     * Get the list of {@link God} the players will be allowed to pick from
     * @return list of allowed gods
     */
    public List<God> getAvailableGods() {
        return availableGods;
    }

    /**
     * Get all configured gods
     * @return list of all available gods
     */
    public List<God> getAllGods() {
        if (allGods == null) {
            try {
                String config = Resources.loadGodConfig(this);
                allGods = Serializer.deserializeGodList(config);
            } catch (JsonSyntaxException e) {
                if (Resources.usingCustomConfig()) {
                    CLI.error("Invalid configuration json, falling back to default");
                    Resources.setGodConfigFile(null);
                    String config = Resources.loadGodConfig(this);
                    allGods = Serializer.deserializeGodList(config);
                }
            }
        }
        return allGods;
    }

    /**
     * Returns true if the lobby contains the maximum number of players
     * @return true if the lobby contains the maximum number of players
     */
    public boolean isLobbyFull() {
        return users.size() == size;
    }

    /**
     * Returns true if the game is full: all users have chosen their god
     * @return true if the game is full
     */
    public boolean isGameFull() {
        return game.getPlayerNumber() == size;
    }

    /**
     * Returns true if the game is full and all players have placed their pawns
     * @return true if the game is full and all players have placed their pawns
     */
    public boolean isGameReady() {
        return size > 0 && readyUsers == size;
    }
}
