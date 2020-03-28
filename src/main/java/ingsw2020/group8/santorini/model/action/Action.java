package ingsw2020.group8.santorini.model.action;

import ingsw2020.group8.santorini.model.board.Coordinate;
import ingsw2020.group8.santorini.model.board.Board;
import ingsw2020.group8.santorini.model.player.Pawn;
import java.lang.UnsupportedOperationException;


public abstract class Action {
    private Effect effect;
    private CheckAllowed checkAllowed;

    public boolean execute(Board board, Pawn pawn, Coordinate coordinate) {
        return effect.execute(board, pawn, coordinate);
    }

    public boolean checkAllowed(Board board, Pawn pawn, Coordinate coordinate) {
        return checkAllowed.isAllowed(board, pawn, coordinate);
    }

    public Action(ActionKind actionKind) {
        switch (actionKind) {
            case MOVE:
                effect = (b,p,c) -> {throw new UnsupportedOperationException();};
                checkAllowed = (b,p,c) -> {throw new UnsupportedOperationException();};
                break;
            case MOVE_UP:
                effect = (b,p,c) -> {throw new UnsupportedOperationException();};
                checkAllowed = (b,p,c) -> {throw new UnsupportedOperationException();};
                break;
            case BUILD_BLOCK:
                effect = (b,p,c) -> {throw new UnsupportedOperationException();};
                checkAllowed = (b,p,c) -> {throw new UnsupportedOperationException();};
                break;
            case BUILD_DOME:
                effect = (b,p,c) -> {throw new UnsupportedOperationException();};
                checkAllowed = (b,p,c) -> {throw new UnsupportedOperationException();};
                break;
            case END_TURN:
                effect = (b,p,c) -> {throw new UnsupportedOperationException();};
                checkAllowed = (b,p,c) -> {throw new UnsupportedOperationException();};
                break;
        }
    }

    public void setEffect(Effect effect) {
        this.effect = effect;
    }

    public void setCheckAllowed(CheckAllowed checkAllowed) {
        this.checkAllowed = checkAllowed;
    }
}
