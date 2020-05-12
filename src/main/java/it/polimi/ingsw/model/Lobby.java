package it.polimi.ingsw.model;

import it.polimi.ingsw.Resources;
import it.polimi.ingsw.controller.messages.User;
import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.board.Coordinate;
import it.polimi.ingsw.model.board.InvalidActionException;
import it.polimi.ingsw.model.player.God;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.serialization.Serializer;

import java.util.*;

public class Lobby {
    public final int PAWN_N = 2;

    private final Game game = new Game();
    private final int size;
    private final List<User> users = new ArrayList<>();
    private final Map<User, Player> userPlayerMap = new HashMap<>();
    private List<God> availableGods = null;
    private int readyUsers = 0;

    public Game getGame() {
        return game;
    }

    public Optional<User> getUserToSetUp() {
        if (readyUsers < size) {
            User user = users.get(readyUsers);
            return Optional.of(user);
        } else {
            return Optional.empty();
        }
    }

    public int getUserNumber() {
        return users.size();
    }

    public Optional<User> getUser(Player player) {
        return users.stream()
                .filter(u -> u.matches(player))
                .findFirst();
    }

    public Optional<Player> getPlayer(User user) {
        return Optional.ofNullable(userPlayerMap.get(user));
    }

    public boolean addUser(User user) {
        if (!users.contains(user) && users.size() < size) {
            users.add(user);
            return true;
        }
        return false;
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
        readyUsers += 1;

        return true;
    }

    public boolean chooseGod(User user, God god) {
        boolean duplicate = game.getPlayers().stream()
                .anyMatch(user::matches);

        if (users.contains(user) && !duplicate && availableGods.contains(god)) {
            Player player = new Player(user, god);
            game.addPlayer(player);
            availableGods.remove(god);
            userPlayerMap.put(user, player);
            return true;
        }
        return false;
    }

    public int getSize() {
        return size;
    }

    public Lobby(int size) {
        this.size = size;
    }

    public List<God> getAvailableGods() {
        return availableGods;
    }

    public void loadGods() {
        String config = Resources.loadGodConfig(this);
        availableGods = Serializer.deserializeGodList(config);
    }

    public boolean isLobbyFull() {
        return getUserNumber() == size;
    }

    public boolean isGameFull() {
        return game.getPlayerNumber() == size;
    }

    public boolean isGameReady() {
        return isGameFull() && readyUsers == size;
    }
}
