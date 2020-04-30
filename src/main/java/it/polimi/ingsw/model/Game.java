package it.polimi.ingsw.model;

import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.player.Player;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private List<Player> players = new ArrayList<>();
    private Board board = new Board();
    private int turn = 0;
    private int eliminationTurn = turn;

    public void addPlayer(Player player) {
        players.add(player);
    }

    public Board getBoard() {
        return board;
    }

    public int getPlayerNumber() {
        return players.size() ;
    }

    public List<Player> getPlayers (){
        return List.copyOf(players); // Returns copy to prevent external mutations
    }

    public int getTurn() {
        return turn;
    }

    public void elimination(Player player) throws IllegalStateException {
        if (players.size() > 0) {
            players.remove(player);
            eliminationTurn = turn;
            } else {
                throw new IllegalStateException();
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
