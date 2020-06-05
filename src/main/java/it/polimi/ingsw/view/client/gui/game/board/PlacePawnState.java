package it.polimi.ingsw.view.client.gui.game.board;

import it.polimi.ingsw.model.board.Coordinate;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseButton;

import java.util.ArrayList;
import java.util.List;

public class PlacePawnState implements BoardClickHandlerState {
    private final List<Coordinate> coords = new ArrayList<>();

    @Override
    public void handleClick(BoardClickHandlerContext ctx, MouseButton btn, Coordinate c) {
        if (btn == MouseButton.PRIMARY) {
            coords.add(c);
            if (coords.size() == 2) {
                ctx.getGameControl().setOnPlaceAttemptListener(r -> onPlaceAttempt(r,ctx));
                ctx.getGameControl().placePawns(coords.get(0), coords.get(1));
                coords.clear();
            }
            ctx.getGameView().getTestLabel().setText("Selected: " + coords.toString());
        }
    }

    @Override
    public void initState(BoardClickHandlerContext ctx) {
        Platform.runLater(()-> ctx.getGameView().getTestLabel().setText("It's your turn! Place your pawns!"));
    }

    private void onPlaceAttempt(Boolean result, BoardClickHandlerContext ctx) {
        if (result) {
            if (ctx.getGameControl().getBoardViewState().getSize() > 1) { //TODO this may not be needed in final version
                ctx.setState(new WaitingState());
            }
        } else {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Invalid position for workers!");
                alert.showAndWait();
            });
        }
        ctx.getGameControl().requestRedraw();
    }
}
