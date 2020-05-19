package it.polimi.ingsw.model;

import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.turn.TurnHelper;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private List<Player> players = new ArrayList<>();
    private Board board = new Board();
    private int turn = 0;
    private TurnHelper turnHelper = new TurnHelper();

    public void addPlayer(Player player) {
        players.add(player);
        addPlayerToTurnManager(player);
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
            removeFromTurnManager(player);
            } else {
                throw new IllegalStateException();
            }
    }

    public void nextTurn() {
        turn++;
        turnHelper.next();
        board.tickCheckEffect();
    }

    public Player getCurrentPlayer() {
        return turnHelper.current();
    }

    public void addPlayerToTurnManager(Player player) {
        turnHelper.add(player);
    }

    public void removeFromTurnManager(Player player) {
        turnHelper.remove(player);
    }
}
