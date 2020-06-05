package it.polimi.ingsw.view.client.gui.game.board;

import it.polimi.ingsw.model.board.Coordinate;
import it.polimi.ingsw.view.client.controls.GameControl;
import it.polimi.ingsw.view.client.gui.game.GameView;
import javafx.scene.input.MouseButton;

public class BoardClickHandlerContext {
    private BoardClickHandlerState state;
    private final GameControl gameControl;
    private final GameView gameView;

    public BoardClickHandlerContext(GameView gameView, GameControl gameControl) {
        this.gameView = gameView;
        this.gameControl = gameControl;
    }

    protected GameView getGameView() {
        return gameView;
    }

    protected GameControl getGameControl() {
        return gameControl;
    }

    public void setState(BoardClickHandlerState state) {
        this.state = state;
        state.initState(this);
    }

    public void handleClick(MouseButton btn, Coordinate c) {
        state.handleClick(this, btn, c);
    }
}
