package it.polimi.ingsw.view.client.gui.game.board;

import it.polimi.ingsw.controller.messages.ActionIdentifier;
import it.polimi.ingsw.model.board.Coordinate;
import it.polimi.ingsw.view.client.controls.BoardViewState;
import it.polimi.ingsw.view.client.controls.PawnViewState;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.util.List;

public class ExecuteActionState implements BoardClickHandlerState {
    private final List<ActionIdentifier> actions;
    private PawnViewState selectedPawn = null;
    private Coordinate target = null;

    public ExecuteActionState(List<ActionIdentifier> actions) {
        this.actions = actions;
    }

    @Override
    public void handleClick(BoardClickHandlerContext ctx, MouseButton btn, Coordinate c) {
        BoardViewState board = ctx.getGameControl().getBoardViewState();

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
                            ctx.getGameView().highlight(c, true);
                        });
                    }
                });
            }
        } else {
            if (btn == MouseButton.PRIMARY) {
                Coordinate oldTarget = target;
                target = c;
                ctx.getGameView().getButtonBar().getButtons().clear();
                for (ActionIdentifier a : actions) {
                    Button button = new Button(a.getDescription().replace('_', ' '));
                    button.setOnMouseClicked(click -> onButtonClick(ctx, click, a));
                    ctx.getGameView().getButtonBar().getButtons().add(button);
                }
                Platform.runLater(() -> {
                    ctx.getGameView().highlight(selectedPawn.getPosition(), true);
                    ctx.getGameView().highlight(target, true);
                    if (oldTarget != null && !oldTarget.equals(target)) {
                        ctx.getGameView().highlight(oldTarget, false);
                    }
                });
            } else if (btn == MouseButton.SECONDARY) {
                target = null;
                selectedPawn = null;
                resetView(ctx);
            }
        }
    }

    @Override
    public void initState(BoardClickHandlerContext ctx) {
        resetView(ctx);
    }

    private void onButtonClick(BoardClickHandlerContext ctx, MouseEvent mouseEvent, ActionIdentifier action) {
        if(mouseEvent.getButton() == MouseButton.PRIMARY) {
            ctx.getGameControl().setOnActionAttemptListener(r -> onExecuteAttempt(ctx, r));
            ctx.getGameControl().executeAction(action, selectedPawn.getId(), target);
        }
    }

    private void onExecuteAttempt(BoardClickHandlerContext ctx, Boolean result) {
        if (result) {
            Platform.runLater(() -> {
                ctx.getGameView().getButtonBar().getButtons().clear();
                ctx.getGameView().highlight(selectedPawn.getPosition(), false);
            });
        } else {
            selectedPawn = null;
            target = null;
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Invalid action!");
                alert.showAndWait();
            });
            resetView(ctx);
        }
    }

    private void resetView(BoardClickHandlerContext ctx) {
        Platform.runLater(() -> {
            ctx.getGameControl().requestRedraw();
            setLabel(ctx, "It's your turn! Click on a Worker, then on a cell and choose your move!");
        });
    }

    private void setLabel(BoardClickHandlerContext ctx, String text) {
        ctx.getGameView().getTestLabel().setText(text);
    }
}
