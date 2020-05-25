package it.polimi.ingsw.view.client.gui.game.board;

import it.polimi.ingsw.controller.messages.ActionIdentifier;
import it.polimi.ingsw.model.board.Coordinate;
import it.polimi.ingsw.view.client.state.BoardViewModel;
import it.polimi.ingsw.view.client.state.PawnViewModel;
import javafx.scene.input.MouseButton;

import java.util.List;

public class ExecuteActionState implements BoardClickHandlerState {
    private final List<ActionIdentifier> actions;
    private PawnViewModel selectedPawn = null;

    public ExecuteActionState(List<ActionIdentifier> actions) {
        this.actions = actions;
    }

    @Override
    public void handleClick(BoardClickHandlerContext ctx, MouseButton btn, Coordinate c) {
        BoardViewModel board = ctx.getGameViewModel().getBoardViewModel();

        if(selectedPawn == null) {
            if (btn == MouseButton.PRIMARY) {
                board.cellAt(c).getPawn().ifPresent(p -> {
                    selectedPawn = p;
                    ctx.getParent().getTestLabel().setText("Selected pawn " + p.getId());
                });
            }
        } else {
            ctx.getParent().getTestLabel().setText("Available actions: " + actions);
        }
    }
}
