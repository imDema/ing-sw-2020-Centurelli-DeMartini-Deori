package it.polimi.ingsw.view.client.controls;

import it.polimi.ingsw.controller.messages.User;
import it.polimi.ingsw.model.board.Building;
import it.polimi.ingsw.model.board.Coordinate;

import java.util.*;

/**
 * Client side board state representation
 */
public class BoardViewState {
    private final int BOARD_SIZE = 5;
    private final CellViewState[][] cells;
    private final List<PawnViewState> pawns = new ArrayList<>();
    private final Map<User, PlayerViewState> userPlayerViewMap = new HashMap<>();
    private User myUser = null;
    private boolean isChallenger = false;
    private int size = 0;

    public BoardViewState() {
        cells = new CellViewState[BOARD_SIZE][BOARD_SIZE];
        for (int i = 0; i < BOARD_SIZE; i++)
            for (int j = 0; j < BOARD_SIZE; j++)
                cells[i][j] = new CellViewState();
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Optional<User> getMyUser() {
        return Optional.ofNullable(myUser);
    }

    public void setMyUser(User user) {
        myUser = user;
    }

    public boolean isChallenger() {
        return isChallenger;
    }

    public void setIsChallenger(boolean isChallenger) {
        this.isChallenger = isChallenger;
    }

    public List<PawnViewState> getPawns() {
        return pawns;
    }

    public void move(Coordinate c1, Coordinate c2) {
        final CellViewState cell1 = cellAt(c1);
        final CellViewState cell2 = cellAt(c2);
        cell1.getPawn().ifPresent(p1 -> {
            p1.setPosition(c2);
            cell1.removePawn();
            cell2.getPawn().ifPresent(p2 -> {
                p2.setPosition(c1);
                cell2.removePawn();
                cell1.putPawn(p2);
            });
            cell2.putPawn(p1);
        });
    }


    public void removePawn(PawnViewState pawn) {
        Coordinate c = pawn.getPosition();
        cellAt(c).removePawn();
    }

    public void putPawn(PawnViewState pawn, Coordinate c) {
        cellAt(c).putPawn(pawn);
        pawn.setPosition(c);
    }

    public void build(Building building, Coordinate c) {
        cellAt(c).setBuilding(building);
    }

    /**
     * Get the cell at the specified position
     * @param c coordinate
     * @return Cell in that position
     */
    public CellViewState cellAt(Coordinate c) { //TODO make optional
        return cells[c.getX()][c.getY()];
    }

    public Optional<PlayerViewState> getPlayer(User user) {
        return Optional.ofNullable(userPlayerViewMap.get(user));
    }

    public Collection<PlayerViewState> getPlayers() {
        return userPlayerViewMap.values();
    }

    public void addPawn(PawnViewState pawn) {
        pawns.add(pawn);
    }

    public void addPlayer(PlayerViewState player) {
        userPlayerViewMap.put(player.getUser(), player);
        size ++;
    }
}
