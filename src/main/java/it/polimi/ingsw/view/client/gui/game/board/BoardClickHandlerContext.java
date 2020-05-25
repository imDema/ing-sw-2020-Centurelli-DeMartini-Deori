package it.polimi.ingsw.view.client.gui.game.board;

import it.polimi.ingsw.model.board.Coordinate;
import it.polimi.ingsw.view.client.gui.game.GameView;
import it.polimi.ingsw.view.client.gui.game.GameViewModel;
import javafx.scene.input.MouseButton;

public class BoardClickHandlerContext {
    private BoardClickHandlerState state;
    private final GameViewModel gameViewModel;
    private final GameView parent;

    protected GameViewModel getGameViewModel() {
        return gameViewModel;
    }

    protected GameView getParent() {
        return parent;
    }

    public BoardClickHandlerContext(GameView parent, GameViewModel gameViewModel) {
        this.parent = parent;
        this.gameViewModel = gameViewModel;
    }

    public void setState(BoardClickHandlerState state) {
        this.state = state;
    }

    public void handleClick(MouseButton btn, Coordinate c) {
        state.handleClick(this, btn, c);
    }
}
