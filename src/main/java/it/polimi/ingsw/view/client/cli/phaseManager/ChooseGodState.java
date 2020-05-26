package it.polimi.ingsw.view.client.cli.phaseManager;

import it.polimi.ingsw.controller.messages.GodIdentifier;
import it.polimi.ingsw.controller.messages.User;
import it.polimi.ingsw.view.cli.CLI;
import it.polimi.ingsw.view.client.ServerHandler;
import it.polimi.ingsw.view.client.state.PlayerViewModel;

import java.util.List;
import java.util.Optional;

public class ChooseGodState implements InputHandlerState {
    private List<GodIdentifier> availableGods;
    private boolean godChosen = false;

    @Override
    public void handle(InputHandlerContext ctx, String line) {
        Optional<User> myUser = ctx.getViewModel().getBoardViewModel().getMyUser();
        if (myUser.isEmpty()) {
            CLI.error("You cannot choose a god as a spectator!");
            return;
        } else if (godChosen) {
            CLI.error("You have already chosen your god!");
            return;
        }

        String[] tokens = line.split(" ");

        if (tokens.length == 1) {
            String godName = tokens[0].toLowerCase();
            availableGods.stream()
                    .filter(g -> g.getName().toLowerCase().equals(godName))
                    .findFirst()
                    .ifPresentOrElse(
                            g -> {
                                ctx.getServerHandler().onChooseGod(myUser.get(), g);
                                ctx.getServerHandler().dispatcher().setOnGodChosenListener((u,god) -> {
                                    if (u.equals(myUser.get())) {
                                        godChosen = true;
                                        CLI.clientInfo("You have chosen " + god.getName());
                                    } else {
                                        CLI.clientInfo(u.getUsername() + " chose " + god.getName());
                                    }
                                    ctx.getViewModel().getBoardViewModel().getPlayer(u)
                                            .ifPresentOrElse(
                                                    p -> p.setGod(god),
                                                    () -> {
                                                        PlayerViewModel player = new PlayerViewModel(u);
                                                        player.setGod(god);
                                                        ctx.getViewModel().getBoardViewModel().addPlayer(player);
                                                    });
                                });
                            },
                            this::printUsage);
        } else {
            printUsage();
        }
    }

    // TODO this is a workaround, should be done better
    public ChooseGodState(ServerHandler serverHandler, List<GodIdentifier> availableGods) {
        this.availableGods = availableGods;
        CLI.clientInfo("Gods: " +
                availableGods.stream().map(GodIdentifier::getName)
                        .reduce("", (res, s) -> res + s + ", ")
                        .replace(",$","")
                + "\n");
        serverHandler.dispatcher().setOnGodsAvailableListener(this::onGodsAvailable);
    }

    private void onGodsAvailable(List<GodIdentifier> godIdentifiers) {
        availableGods = godIdentifiers;
        CLI.clientInfo("Gods: " +
                availableGods.stream().map(GodIdentifier::getName)
                        .reduce("", (res, s) -> res + s + ", ")
                        .replace(",$","")
                + "\n");
    }

    private void printUsage() {
        System.out.println("Choose one of the gods in the list");
        System.out.flush();
    }
}
