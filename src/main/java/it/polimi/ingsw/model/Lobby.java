package it.polimi.ingsw.model;

import it.polimi.ingsw.Resources;
import it.polimi.ingsw.controller.messages.User;
import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.board.Coordinate;
import it.polimi.ingsw.model.board.InvalidActionException;
import it.polimi.ingsw.model.player.God;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.turn.CircularList;
import it.polimi.ingsw.serialization.Serializer;

import java.util.*;

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

    public boolean setChallenger(User challenger) {
        if(users.contains(challenger)) {
            this.challenger = challenger;
            return true;
        }
        return false;
    }

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

    public boolean choseFirstPlayer() {
        return choseFirstPlayer;
    }

    public Optional<User> getChallenger() {
        return Optional.ofNullable(challenger);
    }

    public List<User> getUsers() {
        return users;
    }

    public Optional<User> getUser(Player player) {
        return Optional.ofNullable(playerUserMap.get(player));
    }

    public Optional<Player> getPlayer(User user) {
        return Optional.ofNullable(userPlayerMap.get(user));
    }

    public int getPlayerNumber() {
        return game.getPlayerNumber();
    }

    public boolean addUser(User user) {
        if (!users.contains(user) && users.size() < size &&
                user.getUsername().length() > 0 && user.getUsername().length() < MAX_NAME_LENGTH) {
            users.add(user);
            return true;
        }
        return false;
    }

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

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        // Only allow setting size once
        if (this.size == 0) {
            this.size = size;
        }
    }

    public void setAvailableGods(List<God> availableGods) {
        this.availableGods = availableGods;
    }

    public List<God> getAvailableGods() {
        return availableGods;
    }

    public List<God> getAllGods() {
        if (allGods == null) {
            String config = Resources.loadGodConfig(this);
            allGods = Serializer.deserializeGodList(config);
        }
        return allGods;
    }

    public boolean isLobbyFull() {
        return users.size() == size;
    }

    public boolean isGameFull() {
        return game.getPlayerNumber() == size;
    }

    public boolean isGameReady() {
        return size > 0 && readyUsers == size;
    }
}
