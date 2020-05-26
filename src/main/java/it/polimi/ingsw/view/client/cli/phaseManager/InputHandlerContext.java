package it.polimi.ingsw.view.client.cli.phaseManager;

import it.polimi.ingsw.view.client.ServerHandler;
import it.polimi.ingsw.view.client.state.GameViewModel;

public class InputHandlerContext {
    private InputHandlerState state;
    private final GameViewModel gameViewModel;
    private final ServerHandler serverHandler; //TODO this should be removed

    public GameViewModel getViewModel() {
        return gameViewModel;
    }

    public ServerHandler getServerHandler() {
        return serverHandler;
    }

    public void handle(String line) {
        state.handle(this, line);
    }

    public void setState(InputHandlerState state) {
        this.state = state;
    }

    public InputHandlerContext(GameViewModel gameViewModel, ServerHandler serverHandler) {
        this.gameViewModel = gameViewModel;
        this.serverHandler = serverHandler;
    }
}
