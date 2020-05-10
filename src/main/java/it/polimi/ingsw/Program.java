package it.polimi.ingsw;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Program {
    private boolean clientCLI = true;
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
            System.out.println("AM8-1.0-SNAPSHOT.jar\n" +
                    "Usage:     [-g|—gui]  [-s|—server]  ip_address  port_number\n" +
                    "\n" +
                    "By default the application is launched  in client mode with a Command Line Interface (CLI)\n" +
                    "\n" +
                    "-s —server:    Launch the application in server mode using ip_address and port_number as the server’s parameters\n" +
                    "\n" +
                    "-g —gui:       Launch the application in client mode with a Graphic User Interface (GUI)\n" +
                    "\n" +
                    "-h —help:      Display this help message");
        }
    }

    private boolean loadConfig(Iterator<String> arguments) {
        String argument;
        // loading arguments
        while (arguments.hasNext()) {
            argument = arguments.next();

            switch (argument) {
                case "-s", "--server" -> {
                    serverMode = true;
                    clientCLI = false;
                }
                case "-g", "--gui" -> {
                    clientGUI = true;
                    clientCLI = false;
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
        if (!(ip != null && port != null)) {
            System.err.println("----------------");
            System.err.println("ERROR: Missing arguments");
            System.err.println("----------------");
        }
        return (ip != null && port != null);
    }

    // This method is executed only if the arguments match a valid configuration
    private void startConfig() {
        if (clientCLI) {
            // TODO: start CLI
            System.out.println("Application starting in client with CLI mode ...");
        }
        if (clientGUI) {
            // TODO: start GUI
            System.out.println("Application starting in client with GUI mode ...");
        }
        if (serverMode) {
            System.out.println("Application starting in server mode ...");
            Server server = new Server(ip, port);
            try {
                server.start();
            } catch (IOException e) {
                System.err.println("Error starting server");
                e.printStackTrace();
            }
        }
    }
}
