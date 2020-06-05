package it.polimi.ingsw.view.client.cli.state;

public class WaitingState implements InputHandlerState {
    @Override
    public void handle(InputHandlerContext ctx, String line) {
        System.out.println("Wait for your turn!");
    }
}
