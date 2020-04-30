package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.messages.User;
import org.junit.jupiter.api.Test;

class GameControllerTest {
    User user = null;

    @Test
    void onChooseGod() {
        GameController gameController = new GameController();
        gameController.setRequestPlacePawnsListener(u -> { user = u;
        });
    }
}