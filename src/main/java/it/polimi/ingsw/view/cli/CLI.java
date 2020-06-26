package it.polimi.ingsw.view.cli;

/**
 * Utility class for CLI rendering
 */
public abstract class CLI {
    public static void info(String message) {
        System.out.println(color("INFO", Colors.GREEN) + ": " + message);
        System.out.flush();
    }

    public static void clientInfo(String message) {
        System.out.println(color(message, Colors.CYAN));
    }

    public static void log(String message) {
        System.out.println(color("LOG", Colors.BLUE) + ": " + message);
        System.out.flush();
    }

    public static void error(String message) {
        System.out.println(color("ERROR", Colors.RED) + ": " + message);
        System.out.flush();
    }

    public static String color(String string, Colors color) {
        return color.escape + string + Colors.RESET.escape;
    }

    /**
     * Get a colored string representation of a boolean value
     * @param value boolean value
     * @return a different string for true and false
     */
    public static String mark(boolean value) {
        return value ? color("@", Colors.GREEN) : color("#", Colors.RED);
    }
}
