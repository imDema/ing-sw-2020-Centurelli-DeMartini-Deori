package it.polimi.ingsw.view.client.gui.game.board;

import it.polimi.ingsw.controller.messages.ActionIdentifier;
import it.polimi.ingsw.model.board.Coordinate;
import it.polimi.ingsw.view.client.state.BoardViewModel;
import it.polimi.ingsw.view.client.state.PawnViewModel;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.util.List;

public class ExecuteActionState implements BoardClickHandlerState {
    private final List<ActionIdentifier> actions;
    private PawnViewModel selectedPawn = null;
    private Coordinate target = null;

    public ExecuteActionState(List<ActionIdentifier> actions) {
        this.actions = actions;
    }

    @Override
    public void handleClick(BoardClickHandlerContext ctx, MouseButton btn, Coordinate c) {
        BoardViewModel board = ctx.getGameViewModel().getBoardViewModel();

        if(selectedPawn == null) {
            if (btn == MouseButton.PRIMARY) {
                board.cellAt(c).getPawn().ifPresent(p -> {
                    boolean myPawn = board.getMyUser()
                            .flatMap(board::getPlayer)
                            .map(player -> p.getOwner().equals(player))
                            .orElse(false);
                    if (myPawn) {
                        selectedPawn = p;
                        Platform.runLater(() -> {
                            setLabel(ctx, "Selected pawn " + p.getId());
                            ctx.getGameView().highlight(c);
                        });
                    }
                });
            }
        } else {
            if (btn == MouseButton.PRIMARY) {
                target = c;
                ctx.getGameView().getButtonBar().getButtons().clear();
                for (ActionIdentifier a : actions) {
                    Button button = new Button(a.getDescription().replace('_', ' '));
                    button.setOnMouseClicked(click -> onButtonClick(ctx, click, a));
                    ctx.getGameView().getButtonBar().getButtons().add(button);
                }
            } else if (btn == MouseButton.SECONDARY) {
                Platform.runLater(() -> {
                    ctx.getGameViewModel().requestRedraw();
                    setLabel(ctx, "");
                });
                target = null;
                selectedPawn = null;
            }
        }
    }

    private void onButtonClick(BoardClickHandlerContext ctx, MouseEvent mouseEvent, ActionIdentifier action) {
        if(mouseEvent.getButton() == MouseButton.PRIMARY) {
            ctx.getGameViewModel().setOnActionAttemptListener(r -> onExecuteAttempt(ctx, r));
            ctx.getGameViewModel().executeAction(action, selectedPawn.getId(), target);
        }
    }

    private void onExecuteAttempt(BoardClickHandlerContext ctx, Boolean result) {
        if (result) {
            Platform.runLater(() -> {
                ctx.getGameView().getButtonBar().getButtons().clear();
                ctx.getGameView().highlight(selectedPawn.getPosition());
            });
        } else {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Invalid action!");
                alert.showAndWait();
            });
        }
    }

    private void setLabel(BoardClickHandlerContext ctx, String text) {
        ctx.getGameView().getTestLabel().setText(text);
    }
}
