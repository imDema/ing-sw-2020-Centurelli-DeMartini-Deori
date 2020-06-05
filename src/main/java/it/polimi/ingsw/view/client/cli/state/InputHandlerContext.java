package it.polimi.ingsw.view.client.cli.state;

import it.polimi.ingsw.view.client.controls.GameControl;

public class InputHandlerContext {
    private InputHandlerState state;
    private final GameControl gameControl;

    public InputHandlerContext(GameControl gameControl) {
        this.gameControl = gameControl;
    }

    public void handle(String line) {
        state.handle(this, line);
    }

    public void setState(InputHandlerState state) {
        this.state = state;
    }

    public GameControl getGameControl() {
        return gameControl;
    }
}
