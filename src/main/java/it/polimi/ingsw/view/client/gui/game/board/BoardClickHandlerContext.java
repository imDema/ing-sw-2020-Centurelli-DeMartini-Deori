package it.polimi.ingsw.view.client.gui.game.board;

import it.polimi.ingsw.model.board.Coordinate;
import it.polimi.ingsw.view.client.gui.game.GameView;
import it.polimi.ingsw.view.client.state.GameViewModel;
import javafx.scene.input.MouseButton;

public class BoardClickHandlerContext {
    private BoardClickHandlerState state;
    private final GameViewModel gameViewModel;
    private final GameView gameView;

    protected GameViewModel getGameViewModel() {
        return gameViewModel;
    }

    protected GameView getGameView() {
        return gameView;
    }

    public BoardClickHandlerContext(GameView gameView, GameViewModel gameViewModel) {
        this.gameView = gameView;
        this.gameViewModel = gameViewModel;
    }

    public void setState(BoardClickHandlerState state) {
        this.state = state;
        state.initState(this);
    }

    public void handleClick(MouseButton btn, Coordinate c) {
        state.handleClick(this, btn, c);
    }
}
