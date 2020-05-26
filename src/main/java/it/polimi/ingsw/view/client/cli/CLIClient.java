package it.polimi.ingsw.view.client.cli;

import it.polimi.ingsw.controller.messages.ActionIdentifier;
import it.polimi.ingsw.controller.messages.GodIdentifier;
import it.polimi.ingsw.controller.messages.User;
import it.polimi.ingsw.model.board.Coordinate;
import it.polimi.ingsw.view.cli.CLI;
import it.polimi.ingsw.view.cli.Colors;
import it.polimi.ingsw.view.client.ProxyController;
import it.polimi.ingsw.view.client.ServerHandler;
import it.polimi.ingsw.view.client.cli.phaseManager.*;
import it.polimi.ingsw.view.client.state.GameViewModel;
import it.polimi.ingsw.view.client.state.PawnViewModel;
import it.polimi.ingsw.view.client.state.PlayerViewModel;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class CLIClient {
    private final ProxyController proxyController;
    private final CLIBoardView cliBoardView = new CLIBoardView();
    private GameViewModel gameViewModel;
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
                    return Optional.of(new Coordinate(number, letter));
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

        gameViewModel = new GameViewModel(serverHandler, cliBoardView.getViewModel());
        inputHandler = new InputHandlerContext(gameViewModel, serverHandler);

        Thread controllerThread = new Thread(serverHandler);
        controllerThread.start();
        Scanner input = new Scanner(System.in);

        inputHandler.setState(new LoginState());

        serverHandler.dispatcher().setOnGodsAvailableListener(gods ->
                inputHandler.setState(new ChooseGodState(serverHandler,gods))
        );

        gameViewModel.setOnRequestPlaceListener(() -> {
            CLI.clientInfo("You must place your pawns");
            inputHandler.setState(new PlacePawnState());
        });

        gameViewModel.setOnRequestWaitListener(() ->
                inputHandler.setState(new WaitingState()));

        gameViewModel.setOnActionsReadyListener(this::onActionsReady);

        gameViewModel.addRedrawListener(this::print);

        serverHandler.dispatcher().setOnEliminationListener(this::onElimination);
        serverHandler.dispatcher().setOnWinListener(this::onWin);
        serverHandler.dispatcher().setOnServerErrorListener(this::onServerError);
        serverHandler.dispatcher().setOnTurnChangeListener(this::onTurnChange);
        serverHandler.dispatcher().setOnUserJoinedListener(this::onUserJoined);
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
        for (PawnViewModel pawn : cliBoardView.getViewModel().getPawns()) {
            if (pawn.getOwner().getUser().equals(user))
                cliBoardView.getViewModel().removePawn(pawn);
        }
    }

    private void onGodChosen(User user, GodIdentifier god) {
        CLI.clientInfo(user.getUsername() + " chose " + god.getName());

        cliBoardView.getViewModel().getPlayer(user)
                .ifPresentOrElse(
                        p -> p.setGod(god),
                        () -> {
                            PlayerViewModel player = new PlayerViewModel(user);
                            player.setGod(god);
                            cliBoardView.getViewModel().addPlayer(player);
                        });
    }

    private void onServerError(String type, String description) {
        CLI.error("Server error: " + type + "\n" + description + "\nterminating client");
        System.exit(0);
    }

    private void onTurnChange(User currentUser, int turn) {
        CLI.clientInfo("Turn " + (turn + 1) + ", current player " + currentUser.getUsername());
    }

    private void onUserJoined(User user) {
        cliBoardView.newPlayer(user);
        CLI.clientInfo("User " + user.getUsername() + " joined\n");
    }

    private void onWin(User user) {
        System.out.println(CLI.color("\n" + user.getUsername() + " won the game!\n\n", Colors.BG_YELLOW));
        System.out.flush();
        System.exit(0);
    }
}
