package it.polimi.ingsw.model;

import it.polimi.ingsw.model.player.God;
import it.polimi.ingsw.model.player.GodFactory;
import it.polimi.ingsw.model.player.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Lobby {
    private List<Player> players = new ArrayList<>();
    private final int size ;

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

    public void addPlayer( Player player) throws IllegalStateException {
        if(players.size() < size){
            players.add(player);
        }
        else
            throw new IllegalStateException();
    }

    public Game createGame() throws IllegalStateException{
        if(players.size() == size)
            return new Game(players);
        else
            throw new IllegalStateException();
    }
}
