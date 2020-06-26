package it.polimi.ingsw.controller.events;

/**
 * Listener for the result of a generic client event
 */
public interface OnResultListener {
    /**
     * A request to the server has been completed
     * @param value result of the request
     */
    void onResult (boolean value);
}
