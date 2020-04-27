package it.polimi.ingsw.controller.events;

public interface OnServerErrorListener {
    void onServerError(String type, String description);
}
