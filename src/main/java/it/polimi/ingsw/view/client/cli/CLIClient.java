package it.polimi.ingsw.view.client.cli;

import it.polimi.ingsw.controller.events.OnServerEventListener;
import it.polimi.ingsw.controller.messages.ActionIdentifier;
import it.polimi.ingsw.controller.messages.GodIdentifier;
import it.polimi.ingsw.controller.messages.User;
import it.polimi.ingsw.model.board.Building;
import it.polimi.ingsw.model.board.Coordinate;
import it.polimi.ingsw.view.cli.CLI;
import it.polimi.ingsw.view.cli.Colors;
import it.polimi.ingsw.view.client.ProxyController;
import it.polimi.ingsw.view.client.ServerHandler;
import it.polimi.ingsw.view.client.state.PawnViewModel;
import it.polimi.ingsw.view.client.state.PlayerViewModel;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class CLIClient implements OnServerEventListener {
    private final ProxyController proxyController;
    private final CLIBoardViewModel cliBoardViewModel = new CLIBoardViewModel();
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
        serverHandler.dispatcher().setOnServerEventListener(this);
        Scanner input = new Scanner(System.in);
        System.out.print("> ");
        System.out.flush();

        while (input.hasNext()) {
            String opCode = input.next().toLowerCase();

            switch (opCode) {
                case "size" -> {
                    serverHandler.dispatcher().setOnResultListener(q -> System.out.print((q ? "" : "N") + "ACK\n> "));
                    serverHandler.onSelectPlayerNumber(input.nextInt());
                }
                case "login" -> {
                    User user = new User(input.next());
                    if (!loggedIn) {
                        serverHandler.dispatcher().setOnResultListener(r -> {
                            if (r) {
                                loggedIn = true;
                                System.out.print("ACK\n> ");
                                serverHandler.dispatcher().setOnResultListener(q -> System.out.print((q ? "" : "N") + "ACK\n> "));
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
                    Optional<Coordinate> c1 = decodeCoordinate(input.next());
                    Optional<Coordinate> c2 = decodeCoordinate(input.next());
                    if(c1.isPresent() && c2.isPresent())
                        serverHandler.onPlacePawns(user, c1.get(), c2.get());
                    else
                        System.out.println("You can't place pawn out of the board");
                }
                case "check" -> {
                    String actionDesc = input.next().toLowerCase();
                    int pawnId;
                    try {
                        pawnId = input.nextInt();
                    } catch (InputMismatchException e) {
                        CLI.error("Please select a pawn");
                        break;
                    }
                    Optional<Coordinate> c = decodeCoordinate(input.next());

                    if (c.isPresent()) {
                        availableActions.stream()
                            .filter(a -> a.getDescription().toLowerCase().equals(actionDesc))
                            .findFirst()
                            .ifPresentOrElse(
                                    a -> serverHandler.onCheckAction(user, pawnId, a, c.get()),
                                    () -> CLI.error("Use an action from the supplied list")
                            );
                    } else {
                        CLI.error("Invalid format for coordinate");
                    }
                }
                case "execute" -> {
                    String actionDesc = input.next().toLowerCase();
                    int pawnId;
                    try {
                        pawnId = input.nextInt();
                    } catch (InputMismatchException e) {
                        CLI.error("Please select a pawn");
                        break;
                    }
                    Optional<Coordinate> c = decodeCoordinate(input.next());
                    if (c.isPresent()) {
                        availableActions.stream()
                            .filter(a -> a.getDescription().toLowerCase().equals(actionDesc))
                            .findFirst()
                            .ifPresentOrElse(
                                    a -> serverHandler.onExecuteAction(user, pawnId, a, c.get()),
                                    () -> CLI.error("Use an action from the supplied list")
                            );
                    } else {
                        CLI.error("Invalid format for coordinate");
                    }
                }
                default -> {
                    System.out.println("COMMANDS:");
                    System.out.println("size N\n" +
                            "login USER\n" +
                            "choose GOD_ID\n" +
                            "place COORDINATE COORDINATE \n" +
                            "check ACTION_ID PAWN_ID COORDINATE\n" +
                            "execute ACTION_ID PAWN_ID COORDINATE");
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
        System.out.println("\n\n\n\n" + cliBoardViewModel.renderBoard());
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
        for(PawnViewModel pawn : cliBoardViewModel.getBoardViewModel().getPawns()){
            if(pawn.getOwner().getUser().equals(user))
                cliBoardViewModel.getBoardViewModel().removePawn(pawn);
        }
    }

    @Override
    public void onGodChosen(User user, GodIdentifier godIdentifier) {
        CLI.info("\nUser " + user + " has chosen the god " + godIdentifier.getName());
        System.out.print("> ");
        System.out.flush();
        cliBoardViewModel.addPlayer(new PlayerViewModel(user), godIdentifier);
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
        System.out.println(CLI.color("\n" + user.getUsername() + " won the game!\n> ", Colors.BG_YELLOW));
        System.out.flush();
        System.exit(0);
    }

    @Override
    public void onBuild(Building building, Coordinate coordinate) {
        //CLI.info("building at " + coordinate + " is now " + building + "\n> ");
        cliBoardViewModel.getBoardViewModel().build(building, coordinate);
        print();
    }

    @Override
    public void onMove(Coordinate from, Coordinate to) {
        cliBoardViewModel.getBoardViewModel().move(from, to);
        print();
    }

    @Override
    public void onPawnPlaced(User owner, int pawnId, Coordinate coordinate) {
        CLI.info("\nUser \"" + owner.getUsername() +
                "\" placed pawn " + pawnId + " at " + (char)(coordinate.getX() + 'A') + (coordinate.getY() + 1));
        System.out.flush();
        PlayerViewModel player = cliBoardViewModel.getBoardViewModel().getPlayer(user);
        PawnViewModel p = new PawnViewModel(player, pawnId);
        player.addPawn(p);
        cliBoardViewModel.getBoardViewModel().putPawn(p, coordinate);
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
