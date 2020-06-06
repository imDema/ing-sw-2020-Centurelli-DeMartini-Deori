package it.polimi.ingsw;

import it.polimi.ingsw.view.client.cli.CLIClient;
import it.polimi.ingsw.view.client.gui.App;
import it.polimi.ingsw.view.server.Server;

import java.util.Arrays;
import java.util.Iterator;

public class Program {
    private boolean cliMode = false;
    private boolean serverMode = false;
    private String ip = null;
    private Integer port = null;

    public static void main(String[] args) {
        Program program = new Program();
        Iterator<String> arguments = Arrays.stream(args).iterator();

        if (program.parseConfig(arguments)) {
            program.start();
        } else {
            System.out.println(
                    "Usage: java -jar AM8-1.0-SNAPSHOT.jar [--cli|--server IP PORT] [--gods JSON]\n" +
                    "\n" +
                    "Launching in server or cli mode requires specifying IP and PORT\n" +
                    "By default the application is launched in graphical mode\n" +
                    "\n" +
                    "-c, --cli:         Launch the application in client mode with a Command Line Interface\n" +
                    "-s, --server:      Start in server mode binding on tcp://IP:PORT\n" +
                    "-g, --gods JSON:   Load god configuration from JSON file (server side)" +
                    "-h, --help:        Display this help message\n" +
                    "Example: java -jar AM8-1.0-SNAPSHOT.jar -c 127.0.0.1 5000");
        }
    }

    private boolean parseConfig(Iterator<String> arguments) {
        String argument;
        // loading arguments
        while (arguments.hasNext()) {
            argument = arguments.next();

            switch (argument) {
                case "-s", "--server" -> serverMode = true;
                case "-c", "--cli" -> cliMode = true;
                case "-g", "--gods" -> {
                    if (!arguments.hasNext()) {
                        System.err.println("----------------");
                        System.err.println("ERROR: Missing parameter for option -g or --gods");
                        System.err.println("----------------");
                        return false;
                    } else {
                        Resources.setGodConfigFile(arguments.next());
                    }
                }
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
        if (!(serverMode || cliMode) || (ip != null && port != null)) {
            return true;
        } else {
            System.err.println("----------------");
            System.err.println("ERROR: Missing arguments IP PORT");
            System.err.println("----------------");
            return false;
        }
    }

    private void start() {
        if (serverMode) {
            Server server = new Server(ip, port);
            server.start();
        } else if (cliMode) {
            CLIClient cliClient = new CLIClient(ip, port);
            cliClient.startClient();
        } else {
            App.main();
        }
    }
}
