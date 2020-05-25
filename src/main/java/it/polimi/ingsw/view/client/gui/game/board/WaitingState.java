package it.polimi.ingsw.view.client.gui.game.board;

import it.polimi.ingsw.model.board.Coordinate;
import javafx.scene.input.MouseButton;

public class WaitingState implements BoardClickHandlerState {
    @Override
    public void handleClick(BoardClickHandlerContext ctx, MouseButton btn, Coordinate c) {
        ctx.getParent().getTestLabel().setText("Wait for your turn!");
    }
}
