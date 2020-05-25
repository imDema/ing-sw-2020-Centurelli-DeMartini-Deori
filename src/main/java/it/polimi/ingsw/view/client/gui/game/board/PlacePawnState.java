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
                ctx.getGameViewModel().setOnPlaceAttemptListener(r -> onPlaceAttempt(r,ctx));
                ctx.getGameViewModel().placePawns(coords.get(0), coords.get(1));
                coords.clear();
            }
            ctx.getParent().getTestLabel().setText("Selected: " + coords.toString());
        }
    }

    private void onPlaceAttempt(Boolean result, BoardClickHandlerContext ctx) {
        if (result) {
            ctx.getGameViewModel().requestRedraw();
            Platform.runLater(() -> ctx.getParent().getTestLabel().setText(""));
        } else {
            Platform.runLater(new Alert(Alert.AlertType.INFORMATION, "Invalid position for workers!")::showAndWait); //TODO remove
        }
    }
}
