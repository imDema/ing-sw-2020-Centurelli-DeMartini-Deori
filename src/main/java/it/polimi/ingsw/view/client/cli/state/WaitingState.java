package it.polimi.ingsw.view.client.cli.state;

/**
 * {@link InputHandlerState} used when a user should wait for something to happen
 */
public class WaitingState implements InputHandlerState {
    @Override
    public void handle(InputHandlerContext ctx, String line) {
        System.out.println("Wait for your turn!");
    }
}
