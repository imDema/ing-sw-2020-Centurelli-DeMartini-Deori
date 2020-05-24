package it.polimi.ingsw.view.client.gui.game.board;

import it.polimi.ingsw.model.board.Coordinate;
import javafx.scene.input.MouseButton;

public class BoardClickHandlerContext {
    private BoardClickHandlerState state;

    public void setState(BoardClickHandlerState state) {
        this.state = state;
    }

    public void handleClick(MouseButton btn, Coordinate c) {
        state.handleClick(this, btn, c);
    }
}
