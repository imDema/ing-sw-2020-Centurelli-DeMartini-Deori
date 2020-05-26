package it.polimi.ingsw.view.client.cli.phaseManager;

public interface InputHandlerState {
    void handle(InputHandlerContext ctx, String line);
}
