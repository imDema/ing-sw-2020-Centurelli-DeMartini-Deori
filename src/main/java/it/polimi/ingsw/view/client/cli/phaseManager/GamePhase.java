package it.polimi.ingsw.view.client.cli.phaseManager;

import it.polimi.ingsw.controller.messages.ActionIdentifier;
import it.polimi.ingsw.controller.messages.User;
import it.polimi.ingsw.model.board.Coordinate;
import it.polimi.ingsw.view.cli.CLI;
import it.polimi.ingsw.view.client.ServerHandler;
import it.polimi.ingsw.view.client.cli.CLIBoardView;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class GamePhase implements Phase {
    private List<ActionIdentifier> availableActions;

    public GamePhase(List<ActionIdentifier> availableActions) {
        this.availableActions = availableActions;
    }

    public void manageState(ServerHandler serverHandler, Scanner input, CLIBoardView cliBoardView) {
        serverHandler.dispatcher().setOnActionsReadyListener(this::onActionsReady);
        User user = cliBoardView.getViewModel().getMyUser().get();
        while (input.hasNext()) {
            String opCode = input.next().toLowerCase();
            if ("check".equals(opCode)) {
                String actionDesc = input.next().toLowerCase();
                int pawnId;
                try {
                    pawnId = input.nextInt();
                } catch (InputMismatchException e) {
                    CLI.error("Please select a pawn");
                    continue;
                }
                Optional<Coordinate> c = decodeCoordinate(input.next());

                if (c.isPresent()) {
                    availableActions.stream()
                            .filter(a -> a.getDescription().toLowerCase().equals(actionDesc))
                            .findFirst()
                            .ifPresentOrElse(
                                    a -> {
                                        serverHandler.onCheckAction(user, pawnId, a, c.get());
                                        CLI.clientInfo("Action allowed");
                                    },
                                    () -> CLI.error("Use an action from the supplied list")
                            );
                } else {
                    CLI.error("Invalid format for coordinate");
                }
            } else {
                if(availableActions.stream().anyMatch(a -> a.getDescription().toLowerCase().equals(opCode))) {
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
                                .filter(a -> a.getDescription().toLowerCase().equals(opCode))
                                .findFirst()
                                .ifPresentOrElse(
                                        a -> serverHandler.onExecuteAction(user, pawnId, a, c.get()),
                                        () -> CLI.error("Use an action from the supplied list\n")
                                );
                    } else {
                        CLI.error("Invalid format for coordinate");
                        System.out.flush();
                    }
                } else {
                    CLI.error("You have to use an action from the supplied list");
                }
            }
        }
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

    public void onActionsReady(User user, List<ActionIdentifier> actions) {
        availableActions = actions;
        CLI.clientInfo("\nAvailable actions for user " + user.getUsername() + ": ");
        availableActions.stream()
                .map(ActionIdentifier::getDescription)
                .forEach(CLI::clientInfo);
        CLI.clientInfo("");
    }
}

