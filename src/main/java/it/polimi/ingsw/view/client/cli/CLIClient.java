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
import it.polimi.ingsw.view.client.state.PawnView;
import it.polimi.ingsw.view.client.state.PlayerView;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class CLIClient implements ServerEventsListener {
    private final ProxyController proxyController;
    private List<GodIdentifier> availableGods = null;
    private List<ActionIdentifier> availableActions = null;
    private User user;
    private boolean loggedIn = false;
    private CLIBoardView board = new CLIBoardView();
    private boolean executedAction =false;
    private boolean endTurn = false;

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
        serverHandler.setServerEventListener(this);
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
                    String s = input.next();
                    Optional<Coordinate> c1 = decodeCoordinate(s);
                    // row - column for pawn2
                    String s1 = input.next();
                    Optional<Coordinate> c2 = decodeCoordinate(s1);
                    if( c1.isPresent() && c2.isPresent())
                        serverHandler.onPlacePawns(user, c1.get(), c2.get());
                    else
                        System.out.println("You can't place pawn out of the board");
                }
                case "check" -> {
                    String actionDesc = input.next().toLowerCase();
                    int pawnId = input.nextInt();
                    String s = input.next();
                    Optional<Coordinate> c = decodeCoordinate(s);

                    c.ifPresent(coordinate -> availableActions.stream()
                            .filter(a -> a.getDescription().toLowerCase().equals(actionDesc))
                            .findFirst()
                            .ifPresentOrElse(
                                    a -> serverHandler.onCheckAction(user, pawnId, a, coordinate),
                                    () -> CLI.error("Use an action from the supplied list")
                            ));
                }
                case "execute" -> {
                    String actionDesc = input.next().toLowerCase();
                    int pawnId = input.nextInt();
                    String s = input.next();
                    Optional<Coordinate> c = decodeCoordinate(s);
                    c.ifPresent(coordinate -> availableActions.stream()
                            .filter(a -> a.getDescription().toLowerCase().equals(actionDesc))
                            .findFirst()
                            .ifPresentOrElse(
                                    a -> serverHandler.onExecuteAction(user, pawnId, a, coordinate),
                                    () -> CLI.error("Use an action from the supplied list")
                            ));
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

    public void print() {
        System.out.println("\n\n\n\n" + board.renderBoard());
        System.out.flush();
    }

    @Override
    public void onActionsReady(User user, List<ActionIdentifier> actions) {
        availableActions = actions;
        CLI.info("\nAvailable actions for user " + user.getUsername() + ": ");
        availableActions.stream()
                .map(ActionIdentifier::getDescription)
                .map(n -> n + ", ")
                .forEach(System.out::print);
        System.out.print("\n> ");
        System.out.flush();
    }

    @Override
    public void onElimination(User user) {
        CLI.info("\n" + user + " eliminated");
        System.out.print("\n> ");
        System.out.flush();
        for(PawnView pawn : board.getPawns()){
            if(pawn.getOwner().getUser().equals(user))
                board.removePawn(pawn);
        }
    }

    @Override
    public void onGodChosen(User user, GodIdentifier godIdentifier) {
        CLI.info("\nUser " + user + " has chosen the god " + godIdentifier.getName());
        System.out.print("> ");
        System.out.flush();
    }

    @Override
    public void onGodsAvailable(List<GodIdentifier> gods) {
        availableGods = gods;
        CLI.info("\nGods: " +
        availableGods.stream().map(GodIdentifier::getName)
                .reduce("", (res, s) -> res + s + ", "));
        System.out.print("> ");
        System.out.flush();
    }

    @Override
    public void onRequestPlacePawns(User user) {
        CLI.info("\nUser " + user + " must place his pawns");
        System.out.print("> ");
        System.out.flush();
    }

    @Override
    public void onServerError(String type, String description) {
        CLI.info("\nServer error: " + type + "\n" + description + "\nterminating client");
        System.exit(0);
    }

    @Override
    public void onTurnChange(User currentUser, int turn) {
        CLI.info("\nTurn " + (turn + 1) + ", current player " + currentUser + "> ");
        System.out.print("> ");
        System.out.flush();
    }

    @Override
    public void onUserJoined(User user) {
        CLI.info("\nUser " + user + " joined");
        System.out.print("> ");
        System.out.flush();
    }

    @Override
    public void onWin(User user) {
        CLI.info("\n" + user + " won the game!\n> ");
        System.out.flush();
    }

    @Override
    public void onBuild(Building building, Coordinate coordinate) {
        //CLI.info("building at " + coordinate + " is now " + building + "\n> ");
        board.build(building, coordinate);
        print();
    }

    @Override
    public void onMove(Coordinate from, Coordinate to) {
        board.move(from, to);
        print();
    }

    @Override
    public void onPawnPlaced(User owner, int pawnId, Coordinate coordinate) {
        CLI.info("\nUser \"" + owner.getUsername() +
                "\" placed pawn " + pawnId + " at " + (char)(coordinate.getX() + 'A') + (coordinate.getY() + 1));
        System.out.flush();
        PlayerView player = board.setUpPlayer(owner);
        PawnView p = new PawnView(player, pawnId);
        player.addPawn(p);
        board.putPawn(p, coordinate);
        print();
        }

    private Optional<Coordinate> decodeCoordinate(String string) {
        if((string.length() == 2)) {
            String s = string.toLowerCase();
            int number;
            int letter = s.charAt(0) - 'a';
            try {
                number = Integer.parseInt(s.substring(1)) - 1;
            }
            catch(NumberFormatException e) {
                return Optional.empty();
                }
            if ((letter >= 0) && (letter <= 5) && (number >= 0 ) && (number <= 5)) {
                return Optional.of(new Coordinate(number, letter));
            } else{
                return Optional.empty();
                }
            } else {
            return Optional.empty();
        }
    }
}
