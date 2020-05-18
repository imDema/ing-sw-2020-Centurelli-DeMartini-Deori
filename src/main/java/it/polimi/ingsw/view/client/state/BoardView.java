package it.polimi.ingsw.view.client.state;

import it.polimi.ingsw.controller.messages.User;
import it.polimi.ingsw.model.board.Building;
import it.polimi.ingsw.model.board.Coordinate;
import it.polimi.ingsw.view.cli.CLI;
import it.polimi.ingsw.view.cli.Colors;

import java.util.*;

public class BoardView {

    private final int BOARD_SIZE = 5;
    private final CellView[][] cells;
    private final List<PawnView> pawns = new ArrayList<>();
    private final Map<User, PlayerView> userPlayerViewMap = new HashMap<>();
    private Stack<String> symbols= new Stack<>();

    public List<PawnView> getPawns() {
        return pawns;
    }

    public BoardView() {
        symbols.push(CLI.color("☻", Colors.GREEN));
        symbols.push(CLI.color("☻", Colors.RED));
        symbols.push(CLI.color("☻", Colors.BG_WHITE));
        symbols.push(CLI.color("☻", Colors.CYAN));
        symbols.push(CLI.color("☻", Colors.PURPLE));
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

    public CellView cellAt(Coordinate c) {
        return cells[c.getX()][c.getY()];
    }

    public PlayerView setUpPlayer(User owner) {
        PlayerView player;
        if(userPlayerViewMap.containsKey(owner))
            player = userPlayerViewMap.get(owner);
        else {
            player = new PlayerView(owner, symbols.pop());
            userPlayerViewMap.put(owner, player);
        }
        return player;
    }
}
