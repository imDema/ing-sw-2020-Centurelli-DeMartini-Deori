package it.polimi.ingsw.view.client.cli.phaseManager;

import it.polimi.ingsw.controller.messages.GodIdentifier;
import it.polimi.ingsw.controller.messages.User;
import it.polimi.ingsw.view.cli.CLI;
import it.polimi.ingsw.view.client.ServerHandler;
import it.polimi.ingsw.view.client.cli.CLIBoardView;

import java.util.List;
import java.util.Scanner;

public class ChooseGodPhase implements Phase {
    private List<GodIdentifier> availableGods;
    private boolean godChosen = false;

    public ChooseGodPhase(List<GodIdentifier> availableGods) {
        this.availableGods = availableGods;
    }

    public void manageState(ServerHandler serverHandler, Scanner input, CLIBoardView cliBoardView) {
        User user = cliBoardView.getViewModel().getMyUser().get();
        while (input.hasNext()) {
            if(godChosen)
                break;
            String opCode = input.next().toLowerCase();
            if (opCode.equals("choose")) {
                String godName = input.next().toLowerCase();
                availableGods.stream()
                        .filter(g -> g.getName().toLowerCase().equals(godName))
                        .findFirst()
                        .ifPresentOrElse(
                                g -> {
                                    serverHandler.onChooseGod(user, g);
                                    godChosen = true;
                                },
                                () -> {
                                    CLI.error("God not found");
                                    System.out.print("> ");
                                    System.out.flush();
                                }
                        );
            } else {
                CLI.error("You have to choose a god from the supplied list");
                System.out.flush();
            }
        }
    }
}
