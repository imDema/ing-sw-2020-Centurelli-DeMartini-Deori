package it.polimi.ingsw.view.client.cli;

import it.polimi.ingsw.controller.messages.ActionIdentifier;
import it.polimi.ingsw.controller.messages.GodIdentifier;
import it.polimi.ingsw.controller.messages.User;
import it.polimi.ingsw.model.board.Coordinate;
import it.polimi.ingsw.view.cli.CLI;
import it.polimi.ingsw.view.cli.Colors;
import it.polimi.ingsw.view.client.ProxyController;
import it.polimi.ingsw.view.client.ServerHandler;
import it.polimi.ingsw.view.client.cli.state.*;
import it.polimi.ingsw.view.client.controls.GameControl;
import it.polimi.ingsw.view.client.controls.PawnViewState;
import it.polimi.ingsw.view.client.controls.PlayerViewState;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class CLIClient {
    private final ProxyController proxyController;
    private final CLIBoardView cliBoardView = new CLIBoardView();
    private Scanner input;
    private GameControl gameControl;
    private InputHandlerContext inputHandler;

    private boolean running = true;

    public CLIClient(String ip, int port) {
        this.proxyController = new ProxyController(ip, port);
    }

    public static Optional<Coordinate> decodeCoordinate(String string) {
        if (string.length() == 2) {
            String s = string.toLowerCase();
            try {
                int letter = s.charAt(0) - 'a';
                int number = Integer.parseInt(s.substring(1)) - 1;
                if (letter >= 0 && letter <= 5 &&
                        number >= 0 && number <= 5) {
                    return Optional.of(new Coordinate(letter, number));
                }
            } catch (NumberFormatException e) {
                return Optional.empty();
            }
        }
        return Optional.empty();
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

        CLI.clientInfo("   _____             _             _       _ \n" +
                "  / ____|           | |           (_)     (_)\n" +
                " | (___   __ _ _ __ | |_ ___  _ __ _ _ __  _ \n" +
                "  \\___ \\ / _` | '_ \\| __/ _ \\| '__| | '_ \\| |\n" +
                "  ____) | (_| | | | | || (_) | |  | | | | | |\n" +
                " |_____/ \\__,_|_| |_|\\__\\___/|_|  |_|_| |_|_|\n" +
                "                                             \n" +
                "                                             \n" +
                "\n" +
                "Welcome to Santorini,\nType size N to choose the game size\nType login USERNAME to log in");
        System.out.flush();

        gameControl = new GameControl(serverHandler, cliBoardView.getBoardViewState());
        inputHandler = new InputHandlerContext(gameControl);
        inputHandler.setState(new LoginState(serverHandler, cliBoardView));

        Thread controllerThread = new Thread(serverHandler);
        controllerThread.start();
        input = new Scanner(System.in);

        serverHandler.dispatcher().setOnGodsAvailableListener(gods ->
                inputHandler.setState(new ChooseGodState(serverHandler, cliBoardView.getBoardViewState(), gods))
        );

        gameControl.setOnRequestPlaceListener(() -> {
            CLI.clientInfo("You must place your pawns");
            inputHandler.setState(new PlacePawnState());
        });

        gameControl.setOnRequestWaitListener(() ->
                inputHandler.setState(new WaitingState()));

        gameControl.setOnActionsReadyListener(this::onActionsReady);

        gameControl.addRedrawListener(this::print);

        serverHandler.dispatcher().setOnEliminationListener(this::onElimination);
        serverHandler.dispatcher().setOnWinListener(this::onWin);
        serverHandler.dispatcher().setOnServerErrorListener(this::onServerError);
        serverHandler.dispatcher().setOnTurnChangeListener(this::onTurnChange);
        serverHandler.dispatcher().setOnGodChosenListener(this::onGodChosen);

        // Start handling inputs
        while (running && input.hasNext()) {
            inputHandler.handle(input.nextLine());
        }

        try {
            controllerThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void print() {
        System.out.println("\n\n\n\n" + cliBoardView.renderBoard());
        System.out.flush();
    }

    private void onActionsReady(List<ActionIdentifier> actions) {
        CLI.clientInfo("Available actions: ");
        actions.stream()
                .map(ActionIdentifier::getDescription)
                .forEach(CLI::clientInfo);
        CLI.clientInfo("");

        inputHandler.setState(new ExecuteActionState(actions));
    }

    private void onElimination(User user) {
        CLI.clientInfo( user.getUsername() + " eliminated");
        for (PawnViewState pawn : cliBoardView.getBoardViewState().getPawns()) {
            if (pawn.getOwner().getUser().equals(user))
                cliBoardView.getBoardViewState().removePawn(pawn);
        }
    }

    private void onGodChosen(User user, GodIdentifier god) {
        CLI.clientInfo(user.getUsername() + " chose " + god.getName());

        cliBoardView.getBoardViewState().getPlayer(user)
                .ifPresentOrElse(
                        p -> p.setGod(god),
                        () -> {
                            PlayerViewState player = new PlayerViewState(user);
                            player.setGod(god);
                            cliBoardView.getBoardViewState().addPlayer(player);
                        });
    }

    private void onServerError(String type, String description) {
        CLI.error("Server error: " + type + "\n" + description + "\nterminating client");
        running = false;
        input.close();
    }

    private void onTurnChange(User currentUser, int turn) {
        CLI.clientInfo("Turn " + (turn + 1) + ", current player " + currentUser.getUsername());
    }

    private void onWin(User user) {
        System.out.println(CLI.color("\n" + user.getUsername() + " won the game!\n\n", Colors.BG_YELLOW));
        System.out.flush();
        running = false;
        input.close();
    }
}
