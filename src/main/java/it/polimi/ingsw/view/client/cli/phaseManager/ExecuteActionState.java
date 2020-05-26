package it.polimi.ingsw.view.client.cli.phaseManager;

import it.polimi.ingsw.controller.messages.ActionIdentifier;
import it.polimi.ingsw.model.board.Coordinate;
import it.polimi.ingsw.view.cli.CLI;
import it.polimi.ingsw.view.client.cli.CLIClient;

import java.util.List;
import java.util.Optional;

public class ExecuteActionState implements InputHandlerState {
    private final List<ActionIdentifier> availableActions;

    public ExecuteActionState(List<ActionIdentifier> availableActions) {
        this.availableActions = availableActions;
    }

    @Override
    public void handle(InputHandlerContext ctx, String line) {
        String[] tokens = line.split(" ");

        if(tokens.length == 3) {
            String actionId = tokens[0].toLowerCase().strip();
            int pawnId;
            try {
                pawnId = Integer.parseInt(tokens[1].strip());
            } catch (NumberFormatException e) {
                CLI.error("Invalid number format for pawn id. Please choose a valid id like 0 or 1");
                printUsage();
                return;
            }
            Optional<Coordinate> target = CLIClient.decodeCoordinate(tokens[2]);
            if (target.isPresent()) {
                findAction(actionId)
                    .ifPresentOrElse(
                            a -> { //TODO move elsewhere
                                ctx.getViewModel().setOnActionAttemptListener(this::onActionAttempt);
                                ctx.getViewModel().executeAction(a, pawnId, target.get());
                            },
                            () -> CLI.error("Use an action from the supplied list\n")
                    );
            } else {
                CLI.error("Invalid format for coordinate");
                printUsage();
            }
        } else {
            printUsage();
        }
    }

    private void onActionAttempt(Boolean result) {
        if (!result) {
            CLI.error("Invalid action! try a different one");
        }
    }

    private Optional<ActionIdentifier> findAction(String actionId) {
        return availableActions.stream()
                .filter(a -> a.getDescription().toLowerCase().equals(actionId))
                .findFirst();
    }

    private void printUsage() {
        System.out.println("You have to use an action from the supplied list\nEXAMPLE:\nmove 0 C3");
        System.out.flush();
    }
}

