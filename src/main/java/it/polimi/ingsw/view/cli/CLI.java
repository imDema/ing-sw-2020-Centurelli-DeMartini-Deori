package it.polimi.ingsw.view.cli;

public abstract class CLI {
    public static void info(String message) {
        System.out.println(color("INFO", Colors.GREEN) + ": " + message);
        System.out.flush();
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

    public static String mark(boolean value) {
        return value ? color("@", Colors.GREEN) : color("#", Colors.RED);
    }
}
