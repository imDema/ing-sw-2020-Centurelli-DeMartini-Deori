package it.polimi.ingsw.view.client.cli.phaseManager;

import it.polimi.ingsw.controller.messages.User;
import it.polimi.ingsw.view.cli.CLI;
import it.polimi.ingsw.view.client.ServerHandler;
import it.polimi.ingsw.view.client.cli.CLIBoardView;

import java.util.Scanner;

public class LoginPhase implements Phase {
    private boolean loggedIn = false;

    public void manageState(ServerHandler serverHandler, Scanner input, CLIBoardView cliBoardView) {
        CLI.clientInfo("Choose the game size and log in\n");
        while (input.hasNext()) {
            if(loggedIn)
                break;
            String opCode = input.next().toLowerCase();
            switch (opCode) {
                case "size" -> {
                    int size = input.nextInt();
                    serverHandler.dispatcher().setOnResultListener(q -> CLI.clientInfo((q ? "" : "Not valid size") ));
                    serverHandler.onSelectPlayerNumber(size);
                }

                case "login" -> {
                    User user = new User(input.next());
                        serverHandler.dispatcher().setOnResultListener(r -> {
                            if (r) {
                                serverHandler.dispatcher().setOnResultListener(q -> System.out.print((q ? "" : "Login failed")));
                                loggedIn = true;
                            } else {
                                CLI.error("Login failed\n> ");
                            }
                        });
                        cliBoardView.getViewModel().setMyUser(user);
                        serverHandler.onAddUser(user);
                }
                default -> {
                    CLI.error("COMMANDS:");
                    CLI.error("size N\n" +
                            "login USER\n");
                    System.out.flush();
                }
            }
        }
    }
}
