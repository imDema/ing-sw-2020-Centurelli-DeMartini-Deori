package it.polimi.ingsw.view.client.gui.game.board;

import it.polimi.ingsw.model.board.Coordinate;
import javafx.application.Platform;
import javafx.scene.input.MouseButton;

public class WaitingState implements BoardClickHandlerState {
    @Override
    public void handleClick(BoardClickHandlerContext ctx, MouseButton btn, Coordinate c) {
        ctx.getGameView().getTestLabel().setText("Wait for your turn!");
    }

    @Override
    public void initState(BoardClickHandlerContext ctx) {
        Platform.runLater(()-> ctx.getGameView().getTestLabel().setText("Wait for your turn!"));
    }
}
