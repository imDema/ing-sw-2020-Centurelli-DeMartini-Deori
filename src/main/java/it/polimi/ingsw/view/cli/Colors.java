package it.polimi.ingsw.view.cli;

public enum Colors {
    RED("\u001B[31m"),
    GREEN("\u001B[32m"),
    YELLOW("\u001B[33m"),
    BLUE("\u001B[34m"),
    PURPLE("\u001B[35m"),
    CYAN("\u001B[36m"),
    BG_RED("\u001B[41;30m"),
    BG_GREEN("\u001B[42;30m"),
    BG_YELLOW("\u001B[43;30m"),
    BG_BLUE("\u001B[44;30m"),
    BG_PURPLE("\u001B[45;30m"),
    BG_CYAN("\u001B[46;30m"),
    BG_WHITE("\u001B[107;30m"),
    BLINK("\u001B[1m"),
    RESET("\u001B[0m");

    public final String escape;

    Colors(String escape) {
        this.escape = escape;
    }
}
