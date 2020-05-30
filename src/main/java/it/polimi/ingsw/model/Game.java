package it.polimi.ingsw.model;

import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.turn.CircularList;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private final List<Player> players = new ArrayList<>();
    private final Board board = new Board();
    private final CircularList<Player> playerTurnList = new CircularList<>();
    private int turn = 0;

    public void addPlayer(Player player) {
        players.add(player);
        playerTurnList.add(player);
    }

    public Board getBoard() {
        return board;
    }

    public int getPlayerNumber() {
        return players.size() ;
    }

    public List<Player> getPlayers (){
        return List.copyOf(players);
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
        turn += 1;
        playerTurnList.next();
        board.tickCheckEffect();
    }

    protected CircularList<Player> getPlayerTurnList() {
        return playerTurnList;
    }

    public Player getCurrentPlayer() {
        return playerTurnList.current();
    }

    public void removeFromTurnManager(Player player) {
        playerTurnList.remove(player);
    }
}
