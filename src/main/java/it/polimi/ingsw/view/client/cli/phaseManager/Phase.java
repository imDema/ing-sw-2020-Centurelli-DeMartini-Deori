package it.polimi.ingsw.view.client.cli.phaseManager;

import it.polimi.ingsw.view.client.ServerHandler;
import it.polimi.ingsw.view.client.cli.CLIBoardView;

import java.util.Scanner;

public interface Phase {
    void manageState(ServerHandler serverHandler, Scanner input, CLIBoardView cliBoardView);
}
