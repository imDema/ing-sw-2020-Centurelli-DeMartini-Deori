package it.polimi.ingsw.model;

import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.turn.CircularList;

import java.util.ArrayList;
import java.util.List;

/**
 * The Game class represents the abstraction of a match with tha players that partecipate
 * and their game turns
 */
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

    /**
     * Eliminates a player from the game
     * @param player Is the player that is eliminated
     * @throws IllegalStateException if the player doesn't exists or the number of players is
     * less than 0
     */
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
