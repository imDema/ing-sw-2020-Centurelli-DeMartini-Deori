package it.polimi.ingsw.view.client.cli.state;

/**
 * @see InputHandlerContext
 */
public interface InputHandlerState {
    /**
     * Handle a line of input
     * @param ctx context
     * @param line input line
     */
    void handle(InputHandlerContext ctx, String line);
}
