package it.polimi.ingsw.view.client.cli.state;

public interface InputHandlerState {
    void handle(InputHandlerContext ctx, String line);
}
