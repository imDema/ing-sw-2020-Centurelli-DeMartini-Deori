package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.action.Action;
import it.polimi.ingsw.model.action.CheckEffect;
import it.polimi.ingsw.model.board.events.OnBuildListener;
import it.polimi.ingsw.model.board.events.OnMoveListener;
import it.polimi.ingsw.model.player.Pawn;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * The Board represents the game board, with pawns and buildings.
 * The board is a square of 5x5 Cells, each one represented by a unique coordinate.
 */
public class Board {
    public static final int BOARD_SIZE = 5;
    private OnMoveListener onMoveListener;
    private OnBuildListener onBuildListener;
    private Cell[][] cells;
    // Turn duration
    private List<ActiveEffect> activeCheckEffects = new ArrayList<>();

    public Board() {
        cells = new Cell[BOARD_SIZE][BOARD_SIZE];
        for (int i = 0; i < BOARD_SIZE; i++)
            for (int j = 0; j < BOARD_SIZE; j++)
                cells[i][j] = new Cell();

    }

    public void setOnMoveListener(OnMoveListener onMoveListener) {
        this.onMoveListener = onMoveListener;
    }

    public void setOnBuildListener(OnBuildListener onBuildListener) {
        this.onBuildListener = onBuildListener;
    }

    public Optional<Pawn> getPawnAt(Coordinate c) {
        return cellAt(c).getPawn();
    }

    public Building getBuildingAt(Coordinate c) {
        return cellAt(c).getBuilding();
    }

    /**
     * @param pawn Is the pawn that will move to the specified coordinate
     * @param c Is the coordinate where the pawn will move on
     * @throws InvalidActionException if there is already a pawn in c or c is out of the Board
     */
    public void movePawn(Pawn pawn, Coordinate c) throws InvalidActionException {
        if (getPawnAt(c).isEmpty()) {
            Coordinate oldC = pawn.getPosition();

            cellAt(oldC).removePawn();
            cellAt(c).putPawn(pawn);

            pawn.setPosition(c);
            if(onMoveListener != null)
                onMoveListener.onMove(oldC, c);
        } else if (!pawn.getPosition().equals(c)) {
            throw new InvalidActionException();
        }
    }

    public void swapPawn(Pawn pawn1, Pawn pawn2) {
        Coordinate c1 = pawn1.getPosition();
        Coordinate c2 = pawn2.getPosition();

        cellAt(c1).putPawn(pawn2);
        cellAt(c2).putPawn(pawn1);

        pawn1.setPosition(c2);
        pawn2.setPosition(c1);
        if(onMoveListener != null)
            onMoveListener.onMove(c1, c2);
    }

    public void buildBlock(Coordinate c) throws InvalidActionException {
        Building b = cellAt(c).getBuilding();
        b.buildBlock();
        if(onBuildListener != null)
            onBuildListener.onBuild(b, c);
    }

    public void buildDome(Coordinate c) throws InvalidActionException {
        Building b = cellAt(c).getBuilding();
        b.buildDome();
        if(onBuildListener != null)
            onBuildListener.onBuild(b, c);
    }

    /**
     * Add the specified {@link Pawn} on the Board in the {@link Coordinate} c
     * @throws InvalidActionException if there is another {@link Pawn} in the specified
     * {@link Coordinate}
     */
    public void putPawn(Pawn pawn, Coordinate c) throws InvalidActionException {
        if (getPawnAt(c).isEmpty()) {
            cellAt(c).putPawn(pawn);
            pawn.setPosition(c);
        } else
            throw new InvalidActionException();
    }

    /**
     * @param action The action that needs to be checked
     * @param pawn the pawn that executes the action
     * @param c the coordinate where the action is executed
     * @return true if the specified pawn is allowed to execute the action on the specified coordinate
     */
    public boolean checkAction(Action action, Pawn pawn, Coordinate c) {
        if (!isOnBoard(c))
            return false;

        boolean flag = activeCheckEffects.stream()
                .map(ActiveEffect::getEffect)
                .allMatch(eff -> eff.isAllowed(this, pawn, c, action));

        return flag && action.checkAllowed(this, pawn, c);
    }

    /**
     * The method calls {@link Action}.execute, the action is supposed to be valid
     * (a call to {@link Action}.chechAction() needs to be done before)
     * @param action the action that is executed
     * @throws InvalidActionException if {@link Action}.execute throws and InvalidActionException
     */
    public boolean executeAction(Action action, Pawn pawn, Coordinate c) throws InvalidActionException {
        return action.execute(this, pawn, c);
    }

    // Ticks down one turn from all active check effects
    public void tickCheckEffect() {
        activeCheckEffects = activeCheckEffects.stream()
                .peek(ActiveEffect::tickTurn)
                .filter(a -> a.getDuration() > 0)
                .collect(Collectors.toList());
    }

    public void addCheckEffect(int duration, CheckEffect check) {
        activeCheckEffects.add(new ActiveEffect(duration, check));
    }

    public void removePawn(Pawn pawn) {
        Coordinate c = pawn.getPosition();
        cellAt(c).removePawn();
    }

    public int countPawns() {
        int count = 0;
        for(Cell[] r : cells) {
            for (Cell c : r) {
                if (c.getPawn().isPresent()) {
                    count += 1;
                }
            }
        }
        return count;
    }

    private Cell cellAt(Coordinate c) {
        return cells[c.getX()][c.getY()];
    }

    public boolean isOnBoard(Coordinate c) {
        return ((c.getX() >= 0) && (c.getX() < BOARD_SIZE)) && ((c.getY() >= 0) && (c.getY() < BOARD_SIZE));
    }

    // Only for testing purposes
    /*public void printBoard() {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (cells[i][j].getPawn().isPresent())
                    System.out.print("  " + cells[i][j].getBuilding().getLevel().toString() + " " + cells[i][j].getPawn().get().getOwner().getUsername() + " " + cells[i][j].getPawn().get().getId() + " ");
                else
                    System.out.print("  " + cells[i][j].getBuilding().getLevel().toString() + " " + "free_ _  ");
            }
            System.out.println(" ");
            System.out.println(" ");
        }
    }*/
}

