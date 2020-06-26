package it.polimi.ingsw.view.client.cli.state;

import it.polimi.ingsw.controller.messages.User;
import it.polimi.ingsw.view.cli.CLI;
import it.polimi.ingsw.view.client.ServerHandler;
import it.polimi.ingsw.view.client.cli.CLIBoardView;
import it.polimi.ingsw.view.client.controls.LoginControl;

/**
 * {@link InputHandlerState} that handles logging in and choosing the size of the game
 */
public class LoginState implements InputHandlerState {
    final LoginControl loginControl;
    final CLIBoardView cliBoardView;

    public LoginState(ServerHandler serverHandler, CLIBoardView cliBoardView) {
        this.cliBoardView =cliBoardView;
        loginControl = new LoginControl(serverHandler, cliBoardView.getBoardViewState());
        loginControl.setOnSizeSetListener(this::onSizeSet);
        loginControl.setOnUserJoinedListener(this::onUserJoined);
    }

    private void onUserJoined(User user) {
        CLI.clientInfo(user.getUsername() + " joined");
        cliBoardView.newPlayer(user);
    }

    private void onSizeSet(Integer integer) {
        CLI.clientInfo("Size set is " + integer);
    }

    @Override
    public void handle(InputHandlerContext ctx, String line) {
        String[] tokens = line.split(" ");
        if(tokens.length == 2) {
            String opCode = tokens[0].toLowerCase();
            String s = tokens[1].toLowerCase();
            switch (opCode) {
                case "size" -> {
                    loginControl.setOnSetSizeAttempt(this::onAttempt);
                    loginControl.setSize(Integer.parseInt(s));
                }
                case "login" -> {
                    loginControl.setOnLoginAttempt(this::onAttempt);
                    loginControl.login(s);
                }
                default -> printUsage();
            }
        } else {
            printUsage();
        }
    }

    private void onAttempt(Boolean r, String s) {
        if(!r)
            CLI.clientInfo(s);
    }

    private void printUsage() {
        System.out.println("Please choose the game size or login\nEXAMPLE:\nsize 3\nlogin username");
        System.out.flush();
    }

}
