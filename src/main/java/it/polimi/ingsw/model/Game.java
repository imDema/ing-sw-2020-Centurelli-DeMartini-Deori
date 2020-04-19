package it.polimi.ingsw.model;

import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.player.God;
import it.polimi.ingsw.model.player.Player;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private List<Player> players = new ArrayList<>();
    private Board board;
    private int turn = 0;
    private int eliminationTurn = turn;
    private List<String> usernameList = new ArrayList<>();

    public void addUsername(String username) {
        usernameList.add(username);
    }

    public int getPlayerNumber() {
        return players.size() ;
    }

    public Player getPlayer ( int i){
        return players.get(i);
    }

    public int getTurn() {
        return turn + 1;
    }

    public void elimination(Player player) throws InvalidStateException {
        if (players.size() > 0) {
            players.remove(player);
            eliminationTurn = turn;
            } else {
                throw new InvalidStateException();
            }
        }


    public void setPlayer(God god) throws InvalidStateException {
        if (players.size() < 3) {
            Player player = new Player(usernameList.get(players.size()));
            player.setGod(god);
            players.add(player);
        } else {
            throw new InvalidStateException();
        }
    }

    public void nextTurn() {
        turn++;
    }

    public Player getCurrentPlayer() {
        if (players.size() == 3) {
            return players.get(turn % 3);
        } else {
            return players.get((turn - eliminationTurn) % 2);
        }
    }
}
