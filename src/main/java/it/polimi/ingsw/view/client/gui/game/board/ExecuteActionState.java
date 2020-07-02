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

/**
 * {@link BoardClickHandlerState} that handles clicks when a player must make a move during its turn
 */
public class ExecuteActionState implements BoardClickHandlerState {
    private List<ActionIdentifier> actions;
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
                        setLabel(ctx, "Now click on a cell and make a move!");
                    }
                });
            }
        } else {
            if (btn == MouseButton.PRIMARY) {
                target = c;
                ctx.getGameView().getButtonBar().getChildren().clear();
                for (ActionIdentifier a : actions) {
                    Button button = new Button(a.getDescription().replace('_', ' '));
                    button.setOnMouseClicked(click -> onButtonClick(ctx, click, a));
                    ctx.getGameView().getButtonBar().getChildren().add(button);
                }
            } else if (btn == MouseButton.SECONDARY) {
                target = null;
                selectedPawn = null;
                resetButtons(ctx);
                setLabel(ctx, "It's your turn! Click on a Worker, then on a cell and choose your move!");
            }
        }
        draw(ctx);
    }

    @Override
    public void initState(BoardClickHandlerContext ctx) {
        Platform.runLater(() ->
            setLabel(ctx, "It's your turn! Click on a Worker, then on a cell and choose your move!"));
        // Override listeners
        ctx.getGameControl().setOnActionsReadyListener(a -> {
            this.actions = a;
            this.target = null;
            Platform.runLater(() -> draw(ctx));
        });
        ctx.getGameControl().setOnRequestWaitListener(() -> {
            // Reset listener
            ctx.getGameControl().setOnActionsReadyListener(a -> ctx.setState(new ExecuteActionState(a)));
            ctx.getGameControl().setOnActionAttemptListener(null);
            Platform.runLater(() -> resetButtons(ctx));
            ctx.setState(new WaitingState());
        });
    }

    private void onButtonClick(BoardClickHandlerContext ctx, MouseEvent mouseEvent, ActionIdentifier action) {
        if(mouseEvent.getButton() == MouseButton.PRIMARY) {
            ctx.getGameControl().setOnActionAttemptListener(r -> onExecuteAttempt(ctx, r));
            ctx.getGameControl().executeAction(action, selectedPawn.getId(), target);
        }
    }

    private void onExecuteAttempt(BoardClickHandlerContext ctx, Boolean result) {
        if (!result) {
            selectedPawn = null;
            target = null;
            Platform.runLater(() -> {
                resetButtons(ctx);
                setLabel(ctx, "It's your turn! Click on a Worker, then on a cell and choose your move!");
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Invalid action!");
                alert.showAndWait();
            });
        }
        Platform.runLater(() -> resetButtons(ctx));
        draw(ctx);
    }

    private void draw(BoardClickHandlerContext ctx) {
        ctx.getGameControl().requestRedraw();
        Platform.runLater(() -> {
            if (selectedPawn != null) {
                ctx.getGameView().highlight(selectedPawn.getPosition(), true);
            }
            if (target != null) {
                ctx.getGameView().highlight(target, true);
            }
        });
    }

    private void resetButtons(BoardClickHandlerContext ctx) {
        ctx.getGameView().getButtonBar().getChildren().clear();
    }

    private void setLabel(BoardClickHandlerContext ctx, String text) {
        ctx.getGameView().getInfoLabel().setText(text);
    }
}
