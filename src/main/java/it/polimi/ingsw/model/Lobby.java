package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.messages.User;
import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.board.Coordinate;
import it.polimi.ingsw.model.board.InvalidActionException;
import it.polimi.ingsw.model.player.God;
import it.polimi.ingsw.model.player.GodFactory;
import it.polimi.ingsw.model.player.Player;

import java.io.IOException;
import java.util.*;

public class Lobby {
    private List<User> users = new ArrayList<>();
    private Map<User, Player> userPlayerMap = new HashMap<>();
    private List<God> availableGods = null;
    private final int size;
    private int readyUsers = 0;
    private Game game = new Game();

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

    public void loadGods() throws IOException {
        GodFactory godFactory = new GodFactory();
        availableGods = godFactory.getGods();
    }

    public boolean isGameFull() {
        return game.getPlayerNumber() == size;
    }

    public boolean isGameReady() {
        return isGameFull() && readyUsers == size;
    }
}
