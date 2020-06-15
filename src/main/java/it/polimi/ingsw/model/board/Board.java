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
 * Model representation of the game board. Contains pawns and buildings and handles persistent effects
 * @see Pawn
 * @see Building
 * @see ActiveEffect
 */
public class Board {
    public static final int BOARD_SIZE = 5;
    private OnMoveListener onMoveListener;
    private OnBuildListener onBuildListener;
    private final Cell[][] cells;
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
        if (isOnBoard(c)) {
            return cellAt(c).getPawn();
        } else {
            return Optional.empty();
        }
    }

    public Building getBuildingAt(Coordinate c) {
        if (isOnBoard(c)) {
            return cellAt(c).getBuilding();
        } else {
            return null;
        }
    }

    /**
     * Move a pawn to a cell
     * @param pawn Moving pawn
     * @param c Destination for the movement
     * @throws InvalidActionException if there is already a pawn in c or c is out of the Board
     */
    public void movePawn(Pawn pawn, Coordinate c) throws InvalidActionException {
        if (!isOnBoard(c)) throw new InvalidActionException();
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

    /**
     * Swap the position of two pawns on the board
     * @param pawn1 first pawn
     * @param pawn2 second pawn
     */
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

    /**
     * Build a block on the board at the specified coordinate
     * @param c Coordinate where the block should be built
     * @throws InvalidActionException if the building is already level 3
     */
    public void buildBlock(Coordinate c) throws InvalidActionException {
        if (!isOnBoard(c)) throw new InvalidActionException();

        Building b = cellAt(c).getBuilding();
        b.buildBlock();
        if(onBuildListener != null)
            onBuildListener.onBuild(b, c);
    }

    /**
     * Build a dome on the board at the specified coordinate
     * @param c Coordinate where the dome should be built
     * @throws InvalidActionException if a dome is already built in that cell
     */
    public void buildDome(Coordinate c) throws InvalidActionException {
        if (!isOnBoard(c)) throw new InvalidActionException();

        Building b = cellAt(c).getBuilding();
        b.buildDome();
        if(onBuildListener != null)
            onBuildListener.onBuild(b, c);
    }

    /**
     * Put a pawn on the board
     * @param pawn pawn to place on the board
     * @param c position for the pawn
     * @throws InvalidActionException if there is another {@link Pawn} in the specified
     * {@link Coordinate}
     */
    public void putPawn(Pawn pawn, Coordinate c) throws InvalidActionException {
        if (!isOnBoard(c)) throw new InvalidActionException();

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
     * Ticks down one turn from all active check effects
     */
    public void tickCheckEffect() {
        activeCheckEffects = activeCheckEffects.stream()
                .peek(ActiveEffect::tickTurn)
                .filter(a -> a.getDuration() > 0)
                .collect(Collectors.toList());
    }

    /**
     * Adds a persistent CheckEffect to the board for the specified number of turns
     * @param duration is the duration of the persistent effect measured in turns
     * @param check persistent effect to add
     */
    public void addCheckEffect(int duration, CheckEffect check) {
        activeCheckEffects.add(new ActiveEffect(duration, check));
    }

    /**
     * Remove a pawn from the board
     * @param pawn pawn to remove
     */
    public void removePawn(Pawn pawn) {
        Coordinate c = pawn.getPosition();
        cellAt(c).removePawn();
    }

    /**
     * @return number of pawns that are currently on the baord
     */
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

    /**
     * Checks if a coordinate is within the bounds of the board
     * @param c Coordinate to check
     * @return true if it's on the board, false otherwise
     */
    public boolean isOnBoard(Coordinate c) {
        return ((c.getX() >= 0) && (c.getX() < BOARD_SIZE)) && ((c.getY() >= 0) && (c.getY() < BOARD_SIZE));
    }

}

