package it.polimi.ingsw.model;

import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.player.God;
import it.polimi.ingsw.model.player.GodFactory;
import it.polimi.ingsw.model.player.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Lobby {
    private List<Player> players = new ArrayList<>();
    private final int size;

    public int getSize() {
        return size;
    }

    public Lobby(int size) {
        this.size = size;
    }

    public God[] getAvailableGods() throws IOException {
        GodFactory godFactory = new GodFactory();
        return godFactory.getGods();
    }

    public boolean addPlayer(Player player) throws IllegalStateException {
        boolean duplicate = players.stream()
                .map(Player::getUsername)
                .anyMatch(u -> u.equals(player.getUsername()));
        if(players.size() < size && !duplicate){
            players.add(player);
            return true;
        }
        else {
            return false;
        }
    }

    public boolean isFull() {
        return players.size() == size;
    }

    public Game createGame() throws IllegalStateException{
        if(players.size() == size)
            return new Game(players);
        else
            throw new IllegalStateException();
    }
}
