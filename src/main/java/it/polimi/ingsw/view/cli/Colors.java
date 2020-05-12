package it.polimi.ingsw.view.cli;

public enum Colors {
    ANSI_RED("\u001B[31m"),
    ANSI_GREEN("\u001B[32m"),
    ANSI_YELLOW("\u001B[33m"),
    ANSI_BLUE("\u001B[34m"),
    ANSI_PURPLE("\u001B[35m"),
    RESET("\u001B[0m");

    public final String escape;

    Colors(String escape) {
        this.escape = escape;
    }
}
