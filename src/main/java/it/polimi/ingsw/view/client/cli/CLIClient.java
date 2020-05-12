package it.polimi.ingsw.view.client.cli;

import it.polimi.ingsw.controller.events.ServerEventsListener;
import it.polimi.ingsw.controller.messages.ActionIdentifier;
import it.polimi.ingsw.controller.messages.GodIdentifier;
import it.polimi.ingsw.controller.messages.User;
import it.polimi.ingsw.model.board.Building;
import it.polimi.ingsw.model.board.Coordinate;
import it.polimi.ingsw.view.cli.CLI;
import it.polimi.ingsw.view.client.ProxyController;
import it.polimi.ingsw.view.client.ServerHandler;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class CLIClient implements ServerEventsListener {
    private final ProxyController proxyController;
    private List<GodIdentifier> availableGods = null;
    private List<ActionIdentifier> availableActions = null;
    private User user;
    private boolean loggedIn = false;

    public CLIClient(String ip, int port) {
        this.proxyController = new ProxyController(ip, port);
    }

    public void startClient() {
        ServerHandler serverHandler;
        try {
             serverHandler = proxyController.start();
        } catch (IOException e) {
            System.err.println("Error connecting to server");
            e.printStackTrace();
            return;
        }

        Thread controllerThread = new Thread(serverHandler);
        controllerThread.start();
        serverHandler.setServerEventsListener(this);
        Scanner input = new Scanner(System.in);
        System.out.print("> ");
        System.out.flush();

        while (input.hasNext()) {
            String opCode = input.next().toLowerCase();

            switch (opCode) {
                case "size" -> {
                    serverHandler.setOnResultListener(q -> System.out.print((q ? "" : "N") + "ACK\n> "));
                    serverHandler.onSelectPlayerNumber(input.nextInt());
                }
                case "login" -> {
                    User user = new User(input.next());
                    if (!loggedIn) {
                        serverHandler.setOnResultListener(r -> {
                            if (r) {
                                loggedIn = true;
                                System.out.print("ACK\n> ");
                                serverHandler.setOnResultListener(q -> System.out.print((q ? "" : "N") + "ACK\n> "));
                            } else {
                                System.out.print("NACK\n> ");
                            }
                        });
                        this.user = user;
                        serverHandler.onAddUser(user);
                    }
                }
                case "choose" -> {
                    String godName = input.next().toLowerCase();
                    availableGods.stream()
                        .filter(g -> g.getName().toLowerCase().equals(godName))
                        .findFirst()
                        .ifPresentOrElse(
                            g -> serverHandler.onChooseGod(user, g),
                            () -> CLI.error("God not found")
                        );
                }
                case "place" -> {
                    // row - column for pawn1
                    Coordinate c1 = new Coordinate(input.nextInt(), input.nextInt());
                    // row - column for pawn2
                    Coordinate c2 = new Coordinate(input.nextInt(), input.nextInt());

                    serverHandler.onPlacePawns(user, c1, c2);
                }
                case "check" -> {
                    String actionDesc = input.next().toLowerCase();
                    int pawnId = input.nextInt();
                    Coordinate c = new Coordinate(input.nextInt(), input.nextInt());

                    availableActions.stream()
                        .filter(a -> a.getDescription().toLowerCase().equals(actionDesc))
                        .findFirst()
                        .ifPresentOrElse(
                                a -> serverHandler.onCheckAction(user, pawnId, a, c),
                                () -> CLI.error("Use an action from the supplied list")
                        );
                }
                case "execute" -> {
                    String actionDesc = input.next().toLowerCase();
                    int pawnId = input.nextInt();
                    Coordinate c = new Coordinate(input.nextInt(), input.nextInt());

                    availableActions.stream()
                            .filter(a -> a.getDescription().toLowerCase().equals(actionDesc))
                            .findFirst()
                            .ifPresentOrElse(
                                    a -> serverHandler.onExecuteAction(user, pawnId, a, c),
                                    () -> CLI.error("Use an action from the supplied list")
                            );
                }
                default -> {
                    System.out.println("COMMANDS:");
                    System.out.println("size N\n" +
                            "login USER\n" +
                            "choose GOD_ID\n" +
                            "place ROW1 COL1 ROW2 COL2 \n" +
                            "check ACTION_ID PAWN_ID ROW COL\n" +
                            "execute ACTION_ID PAWN_ID  ROW COL");
                }
            }
            System.out.print("> ");
            System.out.flush();
        }
        try {
            controllerThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActionsReady(User user, List<ActionIdentifier> actions) {
        availableActions = actions;
        CLI.info("Available actions for user " + user.getUsername() + ": ");
        availableActions.stream()
                .map(ActionIdentifier::getDescription)
                .map(n -> n + ", ")
                .forEach(System.out::print);
        System.out.print("\n> ");
        System.out.flush();
    }

    @Override
    public void onElimination(User user) {
        CLI.info(user + " eliminated");
        System.out.print("\n> ");
        System.out.flush();
    }

    @Override
    public void onGodChosen(User user, GodIdentifier godIdentifier) {
        CLI.info("User " + user + " has chosen the god " + godIdentifier.getName());
        System.out.print("> ");
        System.out.flush();
    }

    @Override
    public void onGodsAvailable(List<GodIdentifier> gods) {
        availableGods = gods;
        CLI.info("Gods: " +
        availableGods.stream().map(GodIdentifier::getName)
                .reduce("", (res, s) -> res + s + ", "));
        System.out.print("> ");
        System.out.flush();
    }

    @Override
    public void onRequestPlacePawns(User user) {
        CLI.info("User " + user + " must place his pawns");
        System.out.print("> ");
        System.out.flush();
    }

    @Override
    public void onServerError(String type, String description) {
        CLI.info("Server error: " + type + "\n" + description + "\nterminating client");
        System.exit(0);
    }

    @Override
    public void onTurnChange(User currentUser, int turn) {
        CLI.info("Turn " + (turn + 1) + ", current player " + currentUser + "> ");
        System.out.print("> ");
        System.out.flush();
    }

    @Override
    public void onUserJoined(User user) {
        CLI.info("User " + user + " joined");
        System.out.print("> ");
        System.out.flush();
    }

    @Override
    public void onWin(User user) {
        System.out.print("INFO: " + user + " won the game!\n> ");
        System.out.flush();
    }

    @Override
    public void onBuild(Building building, Coordinate coordinate) {
        System.out.print("INFO: building at " + coordinate + " is now " + building + "\n> ");
        System.out.flush();
    }

    @Override
    public void onMove(Coordinate from, Coordinate to) {
        System.out.print("INFO: pawn moved from: " + from + " to: " + to + "\n> ");
        System.out.flush();
    }

    @Override
    public void onPawnPlaced(User owner, int pawnId, Coordinate coordinate) {
        CLI.info("User \"" + owner.getUsername() +
                "\" placed pawn " + pawnId + " at " + coordinate);
        System.out.flush();
    }
}
