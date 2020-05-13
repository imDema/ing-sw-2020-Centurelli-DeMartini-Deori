package it.polimi.ingsw.view.messages;

public enum MessageId {
    ACTION_READY,
    ADD_USER,
    BUILD,
    CHECK_ACTION,
    CHOOSE_GOD,
    ELIMINATION,
    EXECUTE_ACTION,
    GOD_CHOSEN,
    GODS_AVAILABLE,
    MOVE,
    PAWN_PLACED,
    PLACE_PAWNS,
    REQUEST_PLACE_PAWNS,
    RESULT,
    SELECT_PLAYER_NUMBER,
    SERVER_ERROR,
    TURN_CHANGE,
    USER_JOINED,
    WIN;

    public boolean clientMessage() {
        return switch (this) {
            case ADD_USER, CHECK_ACTION, CHOOSE_GOD, EXECUTE_ACTION, PLACE_PAWNS, SELECT_PLAYER_NUMBER  -> true;
            default -> false;
        };
    }

    public boolean serverMessage() {
        return !clientMessage();
    }
}
