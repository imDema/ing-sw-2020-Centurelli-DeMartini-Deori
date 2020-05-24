package it.polimi.ingsw.view.client.gui.game.board;

import it.polimi.ingsw.model.board.Coordinate;
import it.polimi.ingsw.view.client.gui.game.GameViewModel;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseButton;

import java.util.ArrayList;
import java.util.List;

public class PlacePawnState implements BoardClickHandlerState {
    private final GameViewModel gameViewModel;
    private final List<Coordinate> coords = new ArrayList<>();


    public PlacePawnState(GameViewModel gameViewModel) {
        this.gameViewModel = gameViewModel;
    }


    @Override
    public void handleClick(BoardClickHandlerContext ctx, MouseButton btn, Coordinate c) {
        if (btn == MouseButton.PRIMARY) {
            coords.add(c);
            if (coords.size() == 2) {
                gameViewModel.setOnPlaceAttemptListener(this::onPlaceAttempt);
                gameViewModel.placePawns(coords.get(0), coords.get(1));
                coords.clear();
            }
        }
    }

    private void onPlaceAttempt(Boolean result) {
        if (result) {
            gameViewModel.requestRedraw();
        } else {
            Platform.runLater(new Alert(Alert.AlertType.INFORMATION, "AASDHBJLKHDABGS")::showAndWait);
        }
    }
}
