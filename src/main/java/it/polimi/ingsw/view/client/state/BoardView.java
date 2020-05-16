package it.polimi.ingsw.view.client.state;

import it.polimi.ingsw.model.board.Building;
import it.polimi.ingsw.model.board.Coordinate;

import java.util.ArrayList;
import java.util.List;

public class BoardView {

    private final int BOARD_SIZE = 5;
    private final CellView[][] cells;
    private final List<PawnView> pawns = new ArrayList<>();

    public List<PawnView> getPawns() {
        return pawns;
    }

    public BoardView() {
        cells = new CellView[BOARD_SIZE][BOARD_SIZE];
        for (int i = 0; i < BOARD_SIZE; i++)
            for (int j = 0; j < BOARD_SIZE; j++)
                cells[i][j] = new CellView();
    }

    public void move(Coordinate c1, Coordinate c2) {
        final CellView cell1 = cellAt(c1);
        final CellView cell2 = cellAt(c2);
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


    public void removePawn(PawnView pawn) {
        Coordinate c = pawn.getPosition();
        cellAt(c).removePawn();
    }

    public void putPawn(PawnView pawn, Coordinate c) {
        cellAt(c).putPawn(pawn);
        pawn.setPosition(c);
    }

    public void build(Building building, Coordinate c) {
        cellAt(c).setBuilding(building);
    }

    private CellView cellAt(Coordinate c) {
        return cells[c.getX()][c.getY()];
    }

}
