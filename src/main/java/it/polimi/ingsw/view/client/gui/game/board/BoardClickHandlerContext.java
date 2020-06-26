package it.polimi.ingsw.view.client.gui.game.board;

import it.polimi.ingsw.model.board.Coordinate;
import it.polimi.ingsw.view.client.controls.GameControl;
import it.polimi.ingsw.view.client.gui.game.GameView;
import javafx.scene.input.MouseButton;

/**
 * Handles mouse clicks on the board during the game phase.
 * State pattern
 * @see BoardClickHandlerState
 */
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

    /**
     * Set the state of the click handler, state pattern
     * @param state new state
     * @see BoardClickHandlerState
     */
    public void setState(BoardClickHandlerState state) {
        this.state = state;
        state.initState(this);
    }

    /**
     * Handle a mouse click. Behaviour is determined by the current state
     * @param btn Button clicked
     * @param c Coordinate of the clicked cell
     */
    public void handleClick(MouseButton btn, Coordinate c) {
        state.handleClick(this, btn, c);
    }
}
