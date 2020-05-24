package it.polimi.ingsw.view.client.gui.game.board;

import it.polimi.ingsw.model.board.Coordinate;
import javafx.scene.input.MouseButton;

public interface BoardClickHandlerState {
    void handleClick(BoardClickHandlerContext ctx, MouseButton btn, Coordinate c);
}
