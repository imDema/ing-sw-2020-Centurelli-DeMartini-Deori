package it.polimi.ingsw.view.client.cli.phaseManager;

import it.polimi.ingsw.controller.messages.User;
import it.polimi.ingsw.view.cli.CLI;
import it.polimi.ingsw.view.client.ServerHandler;

public class LoginState implements InputHandlerState {
    boolean loggedIn = false;

    @Override
    public void handle(InputHandlerContext ctx, String line) {
        if (loggedIn) {
            CLI.error("Already logged in!");
            return;
        }

        String[] tokens = line.split(" ");

        ServerHandler server = ctx.getServerHandler();

        if(tokens.length == 2) {
            String opCode = tokens[0].toLowerCase();
            switch (opCode) {
                case "size" -> {
                    try {
                        int size = Integer.parseInt(tokens[1]);
                        server.dispatcher().setOnResultListener(q -> CLI.clientInfo((q ? "" : "Invalid valid size or size is already set") ));
                        server.onSelectPlayerNumber(size);
                    } catch (NumberFormatException e) {
                        CLI.error("Invalid number format for size!");
                        printUsage();
                    }
                }
                case "login" -> {
                    User user = new User(tokens[1].strip());
                    server.dispatcher().setOnResultListener(r -> {
                        if (r) {
                            ctx.getViewModel().getBoardViewModel().setMyUser(user);
                            loggedIn = true;
                        } else {
                            CLI.error("Login failed\n> ");
                        }
                    });
                    server.onAddUser(user);
                }
                default -> printUsage();
            }
        } else {
            printUsage();
        }
    }

    private void printUsage() {
        System.out.println("Please choose the game size or login\nEXAMPLE:\nsize 3\nlogin username");
        System.out.flush();
    }
}
