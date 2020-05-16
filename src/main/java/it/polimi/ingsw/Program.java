package it.polimi.ingsw;

import it.polimi.ingsw.view.client.cli.CLIClient;
import it.polimi.ingsw.view.client.gui.GUIClient;
import it.polimi.ingsw.view.server.Server;

import java.util.Arrays;
import java.util.Iterator;

public class Program {
    private boolean clientGUI = false;
    private boolean serverMode = false;
    private String ip = null;
    private Integer port = null;

    public static void main(String[] args) {
        Program program = new Program();
        Iterator<String> arguments = Arrays.stream(args).iterator();

        if (program.loadConfig(arguments)) {
            program.startConfig();
        } else {
            System.out.println(
                    "Usage: java -jar AM8-1.0-SNAPSHOT.jar [-g|--gui] [-s|--server] [IP PORT]\n" +
                    "\n" +
                    "Launching in server or cli mode requires specifying IP and PORT\n" +
                    "By default the application is launched in cli mode\n" +
                    "\n" +
                    "-s, --server:    Launch the application in server mode using ip_address and port_number as the server’s parameters\n" +
                    "-g, --gui:       Launch the application in client mode with a Graphic User Interface (GUI)\n" +
                    "-h, --help:      Display this help message\n" +
                    "Example: java -jar AM8-1.0-SNAPSHOT.jar 127.0.0.1 5000");
        }
    }

    private boolean loadConfig(Iterator<String> arguments) {
        String argument;
        // loading arguments
        while (arguments.hasNext()) {
            argument = arguments.next();

            switch (argument) {
                case "-s", "--server" -> serverMode = true;
                case "-g", "--gui" -> clientGUI = true;
                case "-h", "--help" -> {
                    return false;
                }
                default -> {
                    if (ip == null) {
                        ip = argument;
                    } else if (port == null) {
                        try {
                            port = Integer.parseInt(argument);
                        } catch (NumberFormatException e) {
                            System.err.println("----------------");
                            System.err.println("ERROR: Port must be a number!");
                            System.err.println("----------------");
                            return false;
                        }
                    } else {
                        System.err.println("----------------");
                        System.err.println("ERROR: Too many arguments!");
                        System.err.println("----------------");
                        return false;
                    }
                }
            }
        }
        if ((!serverMode && clientGUI ) || (ip != null && port != null)) {
            return true;
        } else {
            System.err.println("----------------");
            System.err.println("ERROR: Missing arguments IP PORT");
            System.err.println("----------------");
            return false;
        }
    }

    // This method is executed only if the arguments match a valid configuration
    private void startConfig() {
        if (serverMode) {
            Server server = new Server(ip, port);
            server.start();
        } else if (clientGUI) {
            GUIClient.main();
        } else {
            CLIClient cliClient = new CLIClient(ip, port);
            cliClient.startClient();
        }
    }
}
