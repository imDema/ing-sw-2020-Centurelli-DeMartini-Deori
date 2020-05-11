package it.polimi.ingsw.view.client.cli;

import it.polimi.ingsw.controller.events.ServerEventsListener;
import it.polimi.ingsw.controller.messages.ActionIdentifier;
import it.polimi.ingsw.controller.messages.GodIdentifier;
import it.polimi.ingsw.controller.messages.User;
import it.polimi.ingsw.model.board.Building;
import it.polimi.ingsw.model.board.Coordinate;
import it.polimi.ingsw.view.client.ProxyController;
import it.polimi.ingsw.view.client.ServerHandler;


import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class CLIClient implements ServerEventsListener {
    private String ip;
    private int port;
    private final ProxyController proxyController = new ProxyController(ip, port);
    private List<GodIdentifier> availableGods = null;
    private List<ActionIdentifier> availableActions = null;
    private User user;
    private boolean loggedIn = false;

    public CLIClient(String ip, int port) {
        this.ip = ip;
        this.port = port;
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
        System.out.print(">");
        System.out.flush();

        while (input.hasNext()) {
            String opCode = input.next().toLowerCase();

            switch (opCode) {
                case "login" -> {
                    User user = new User(input.next());
                    if (!loggedIn) {
                        serverHandler.setOnResultListener(r -> loggedIn = true);
                        this.user = user;
                        serverHandler.onAddUser(user);
                    }

                }
                case "choose" -> {
                    String godName = input.next();
                    availableGods.stream()
                        .filter(g -> g.getName().equals(godName))
                        .findFirst()
                        .ifPresentOrElse(
                            g -> serverHandler.onChooseGod(user, g),
                            () -> System.out.println("God not found")
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
                    int pawnId = input.nextInt();
                    ActionIdentifier actionIdentifier = availableActions.get(input.nextInt());
                    Coordinate c = new Coordinate(input.nextInt(), input.nextInt());
                    serverHandler.onCheckAction(user, pawnId, actionIdentifier, c);
                }
                case "execute" -> {
                    int pawnId = input.nextInt();
                    ActionIdentifier actionIdentifier = availableActions.get(input.nextInt());
                    Coordinate c = new Coordinate(input.nextInt(), input.nextInt());
                    serverHandler.onExecuteAction(user, pawnId, actionIdentifier, c);
                }
                default -> {
                    System.out.println("COMMANDS:");
                    System.out.println("login USER\n" +
                            "choose GOD_ID\n" +
                            "place ROW1 COL1 ROW2 COL2 \n" +
                            "check PAWN_ID ACTION_ID ROW COL\n" +
                            "execute PAWN_ID ACTION_ID ROW COL");
                }
            }
            System.out.print(">");
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
        System.out.println("INFO: Available actions for user " + user.getUsername() + ": ");
        availableActions.stream()
                .map(ActionIdentifier::getDescription)
                .forEach(System.out::println);
    }

    @Override
    public void onElimination(User user) {
        System.out.println("INFO: " + user + " eliminated");
    }

    @Override
    public void onGodChosen(User user, GodIdentifier godIdentifier) {
        System.out.println("INFO: User " + user + " has chosen the god " + godIdentifier.getName());
    }

    @Override
    public void onGodsAvailable(List<GodIdentifier> gods) {
        availableGods = gods;
        availableGods.stream().map(GodIdentifier::getName)
                .map(n -> n + ", ")
                .forEach(System.out::print);
    }

    @Override
    public void onRequestPlacePawns(User user) {
        System.out.println("INFO: User " + user + " has to place his pawns");
    }

    @Override
    public void onServerError(String type, String description) {
        System.out.println("INFO: Server error: " + type + "\n" + description + "\nterminating client");
        System.exit(0);
    }

    @Override
    public void onTurnChange(User currentUser, int turn) {
        System.out.println("INFO: " + currentUser + " ended turn " + turn);
    }

    @Override
    public void onUserJoined(User user) {
        System.out.println("INFO: User " + user + " joined");
    }

    @Override
    public void onWin(User user) {
        System.out.println("INFO: " + user + " won the game");
    }

    @Override
    public void onBuild(Building building, Coordinate coordinate) {
        System.out.println("INFO: " + building + " built on (" + coordinate.getX() + "," + coordinate.getY() + ")");
    }

    @Override
    public void onMove(Coordinate from, Coordinate to) {
        System.out.println("INFO: current player moved from " +
                from.getX() + "," + from.getY() + " to " +
                to.getX() + "," + to.getY());
    }
}
