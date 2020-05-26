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
import it.polimi.ingsw.view.client.cli.phaseManager.ChooseGodPhase;
import it.polimi.ingsw.view.client.cli.phaseManager.GamePhase;
import it.polimi.ingsw.view.client.cli.phaseManager.PhaseManager;
import it.polimi.ingsw.view.client.cli.phaseManager.PlacePawnPhase;
import it.polimi.ingsw.view.client.state.BoardViewModel;
import it.polimi.ingsw.view.client.state.PawnViewModel;
import it.polimi.ingsw.view.client.state.PlayerViewModel;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class CLIClient implements OnServerEventListener {
    private final ProxyController proxyController;
    private final CLIBoardView cliBoardView = new CLIBoardView();
    private List<GodIdentifier> availableGods = null;
    private List<ActionIdentifier> availableActions = null;
    private final PhaseManager currentPhase = new PhaseManager();

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

        currentPhase.getCurrentPhase().manageState(serverHandler, input, cliBoardView);
        currentPhase.setCurrentPhase(new ChooseGodPhase(availableGods));
        currentPhase.getCurrentPhase().manageState(serverHandler, input, cliBoardView);
        currentPhase.setCurrentPhase(new PlacePawnPhase());
        currentPhase.getCurrentPhase().manageState(serverHandler, input, cliBoardView);
        currentPhase.setCurrentPhase(new GamePhase(availableActions));
        currentPhase.getCurrentPhase().manageState(serverHandler, input, cliBoardView);
        currentPhase.getCurrentPhase().manageState(serverHandler, input, cliBoardView);
        currentPhase.setCurrentPhase(new ChooseGodPhase(availableGods));
        currentPhase.getCurrentPhase().manageState(serverHandler, input, cliBoardView);
        currentPhase.setCurrentPhase(new PlacePawnPhase());

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

    @Override
    public void onActionsReady(User user, List<ActionIdentifier> actions) {
        availableActions = actions;
        if (myUser(user)) {
            CLI.clientInfo("\nAvailable actions for user " + user.getUsername() + ": ");
            availableActions.stream()
                    .map(ActionIdentifier::getDescription)
                    .forEach(CLI::clientInfo);
            CLI.clientInfo("");
        }
    }

    @Override
    public void onElimination(User user) {
        CLI.clientInfo("\n" + user.getUsername() + " eliminated");
        for(PawnViewModel pawn : cliBoardView.getViewModel().getPawns()){
            if(pawn.getOwner().getUser().equals(user))
                cliBoardView.getViewModel().removePawn(pawn);
        }
    }

    @Override
    public void onGodChosen(User user, GodIdentifier godIdentifier) {
        cliBoardView.getViewModel().getPlayer(user).ifPresentOrElse(
                p -> p.setGod(godIdentifier),
                () -> {
                    PlayerViewModel player = new PlayerViewModel(user);
                    player.setGod(godIdentifier);
                    cliBoardView.getViewModel().addPlayer(player);
                });
        CLI.clientInfo("\nUser " + user.getUsername() + " chose the god " + godIdentifier.getName() + "\n");
    }

    @Override
    public void onGodsAvailable(List<GodIdentifier> gods) {
        availableGods = gods;
        CLI.clientInfo("Gods: " +
        availableGods.stream().map(GodIdentifier::getName)
                .reduce("", (res, s) -> res + s + ", ") + "\n");
    }

    @Override
    public void onRequestPlacePawns(User user) {
        CLI.clientInfo("User " + user.getUsername() + " must place his pawns\n");
    }

    @Override
    public void onServerError(String type, String description) {
        CLI.error("\nServer error: " + type + "\n" + description + "\nterminating client");
        System.exit(0);
    }

    @Override
    public void onTurnChange(User currentUser, int turn) {
        CLI.clientInfo("\nTurn " + (turn + 1) + ", current player " + currentUser.getUsername());
    }

    @Override
    public void onUserJoined(User user) {
        cliBoardView.newPlayer(user);
        CLI.clientInfo("\nUser " + user.getUsername() + " joined\n");
    }

    @Override
    public void onWin(User user) {
        System.out.println(CLI.color("\n" + user.getUsername() + " won the game!\n> ", Colors.BG_YELLOW));
        System.out.flush();
        System.exit(0);
    }

    @Override
    public void onBuild(Building building, Coordinate coordinate) {
        cliBoardView.getViewModel().build(building, coordinate);
        print();
    }

    @Override
    public void onMove(Coordinate from, Coordinate to) {
        cliBoardView.getViewModel().move(from, to);
        print();
    }

    @Override
    public void onPawnPlaced(User owner, int pawnId, Coordinate coordinate) {
        BoardViewModel board = cliBoardView.getViewModel();
        PlayerViewModel player = board.getPlayer(owner).orElseThrow();
        PawnViewModel p = new PawnViewModel(player, pawnId);
        player.addPawn(p);
        board.putPawn(p, coordinate);
        print();
    }

    private boolean myUser(User user) {
        return cliBoardView.getViewModel().getMyUser()
                .map(user::equals)
                .orElse(Boolean.FALSE);
    }
}
