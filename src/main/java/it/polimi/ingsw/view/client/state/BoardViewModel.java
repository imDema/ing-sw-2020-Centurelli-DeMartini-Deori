package it.polimi.ingsw.view.client.state;

import it.polimi.ingsw.controller.messages.User;
import it.polimi.ingsw.model.board.Building;
import it.polimi.ingsw.model.board.Coordinate;

import java.util.*;

public class BoardViewModel {

    private final int BOARD_SIZE = 5;
    private final CellViewModel[][] cells;
    private final List<PawnViewModel> pawns = new ArrayList<>();
    private final Map<User, PlayerViewModel> userPlayerViewMap = new HashMap<>();
    private User myUser = null;

    public BoardViewModel() {
        cells = new CellViewModel[BOARD_SIZE][BOARD_SIZE];
        for (int i = 0; i < BOARD_SIZE; i++)
            for (int j = 0; j < BOARD_SIZE; j++)
                cells[i][j] = new CellViewModel();
    }

    public Optional<User> getMyUser() {
        return Optional.ofNullable(myUser);
    }

    public void setMyUser(User user) {
        myUser = user;
    }

    public List<PawnViewModel> getPawns() {
        return pawns;
    }

    public void move(Coordinate c1, Coordinate c2) {
        final CellViewModel cell1 = cellAt(c1);
        final CellViewModel cell2 = cellAt(c2);
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


    public void removePawn(PawnViewModel pawn) {
        Coordinate c = pawn.getPosition();
        cellAt(c).removePawn();
    }

    public void putPawn(PawnViewModel pawn, Coordinate c) {
        cellAt(c).putPawn(pawn);
        pawn.setPosition(c);
    }

    public void build(Building building, Coordinate c) {
        cellAt(c).setBuilding(building);
    }

    public CellViewModel cellAt(Coordinate c) {
        return cells[c.getX()][c.getY()];
    }

    public Optional<PlayerViewModel> getPlayer(User user) {
        return Optional.ofNullable(userPlayerViewMap.get(user));
    }

    public Collection<PlayerViewModel> getPlayers() {
        return userPlayerViewMap.values();
    }

    public void addPawn(PawnViewModel pawn) {
        pawns.add(pawn);
    }

    public void addPlayer(PlayerViewModel player) {
        userPlayerViewMap.put(player.getUser(), player);
    }
}
