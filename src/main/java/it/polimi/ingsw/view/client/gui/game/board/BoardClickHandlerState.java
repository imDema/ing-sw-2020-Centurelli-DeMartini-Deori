package it.polimi.ingsw.view.client.gui.game.board;

import it.polimi.ingsw.model.board.Coordinate;
import javafx.scene.input.MouseButton;

/**
 * @see BoardClickHandlerContext
 */
public interface BoardClickHandlerState {
    /**
     * Handle a mouse click
     * @param btn Button clicked
     * @param ctx context
     * @param c Coordinate of the clicked cell
     */
    void handleClick(BoardClickHandlerContext ctx, MouseButton btn, Coordinate c);

    /**
     * Initialize the state
     * @param ctx context
     */
    void initState(BoardClickHandlerContext ctx);
}
