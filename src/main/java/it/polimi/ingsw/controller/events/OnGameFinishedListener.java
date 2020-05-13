package it.polimi.ingsw.controller.events;

import it.polimi.ingsw.controller.GameController;

public interface OnGameFinishedListener {
    void onGameFinished(GameController controller);
}
