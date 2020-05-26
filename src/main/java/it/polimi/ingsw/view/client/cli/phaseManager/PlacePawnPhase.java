package it.polimi.ingsw.view.client.cli.phaseManager;

import it.polimi.ingsw.controller.messages.User;
import it.polimi.ingsw.model.board.Coordinate;
import it.polimi.ingsw.view.cli.CLI;
import it.polimi.ingsw.view.client.ServerHandler;
import it.polimi.ingsw.view.client.cli.CLIBoardView;

import java.util.Optional;
import java.util.Scanner;

public class PlacePawnPhase implements Phase {
    private boolean pawnPlaced = false;

    public void manageState(ServerHandler serverHandler, Scanner input,  CLIBoardView cliBoardView) {
        User user = cliBoardView.getViewModel().getMyUser().get();
        while (input.hasNext()) {
            if(pawnPlaced)
                break;
            String opCode = input.next().toLowerCase();
            if (opCode.equals("place")) {
                Optional<Coordinate> c1 = decodeCoordinate(input.next());
                Optional<Coordinate> c2 = decodeCoordinate(input.next());
                if (c1.isPresent() && c2.isPresent()) {
                    pawnPlaced = true;
                    serverHandler.onPlacePawns(user, c1.get(), c2.get());
                } else {
                    CLI.error("You can't place pawn out of the board");
                    System.out.flush();
                }
            } else {
                CLI.error("You have to place your two pawns on the board");
                System.out.flush();
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
}
