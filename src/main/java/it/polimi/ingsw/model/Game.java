package it.polimi.ingsw.model;

import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.turn.CircularList;

import java.util.ArrayList;
import java.util.List;

/**
 * Model representation of a game of Santorini.
 * Handles players and turns
 * @see Board
 * @see Player
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

    /**
     * Get the number of {@link Player} in this game
     * @return Number of players in the game
     */
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
     * Remove a player from the game
     * @param player Player to remove
     */
    public void elimination(Player player) {
        if (players.size() > 0) {
            if (player.getPawn(0).getPosition() != null) {
                board.removePawn(player.getPawn(0));
            }
            if (player.getPawn(1).getPosition() != null) {
                board.removePawn(player.getPawn(1));
            }
            playerTurnList.remove(player);
            players.remove(player);
        }
    }

    /**
     * Advance the game to the next turn
     */
    public void nextTurn() {
        turn += 1;
        playerTurnList.next();
        board.tickCheckEffect();
    }

    protected CircularList<Player> getPlayerTurnList() {
        return playerTurnList;
    }

    /**
     * @return Player that is supposed to make a move
     */
    public Player getCurrentPlayer() {
        return playerTurnList.current();
    }
}
