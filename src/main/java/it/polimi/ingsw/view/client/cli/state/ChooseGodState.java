package it.polimi.ingsw.view.client.cli.state;

import it.polimi.ingsw.controller.messages.GodIdentifier;
import it.polimi.ingsw.controller.messages.User;
import it.polimi.ingsw.view.cli.CLI;
import it.polimi.ingsw.view.client.ServerHandler;
import it.polimi.ingsw.view.client.controls.BoardViewState;
import it.polimi.ingsw.view.client.controls.GodSelectorControl;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * {@link InputHandlerState} that handles the selection of available gods and choosing a god
 */
public class ChooseGodState implements InputHandlerState {
    private final GodSelectorControl godSelectorControl;
    private List<GodIdentifier> availableGods;
    private Consumer<String> inputHandler;


    public ChooseGodState(ServerHandler serverHandler, BoardViewState boardViewState, List<GodIdentifier> availableGods) {
        godSelectorControl = new GodSelectorControl(serverHandler, boardViewState);
        this.availableGods = availableGods;
        CLI.clientInfo("Gods: " +
                availableGods.stream().map(GodIdentifier::getName)
                        .reduce("", (res, s) -> res + s + ", ")
                        .replace(",$","")
                + "\n");
        inputHandler = this::selectGods;
        godSelectorControl.setOnGodsAvailable(this::onGodsAvailable);
        godSelectorControl.setOnChooseFirstPlayer(this::chooseFirstPlayer);
        godSelectorControl.setOnWaitForChallenger(this::onWaitForChallenger);
    }

    @Override
    public void handle(InputHandlerContext ctx, String line) {
        inputHandler.accept(line.toLowerCase());
    }

    private void selectGods(String s) {
        List<GodIdentifier> selectedGods = new ArrayList<>();
        String[] tokens = s.split(" ");
        int size = godSelectorControl.getLobbySize();
        if(tokens.length == size) {
            for(int i = 0; i < size; i++) {
                String godName = tokens[i];
                availableGods.stream()
                        .filter(g -> g.getName().toLowerCase().equals(godName))
                        .findFirst()
                        .ifPresentOrElse(
                                g -> {
                                    selectedGods.add(g);
                                    godSelectorControl.selectGods(selectedGods);
                                },
                                this::printUsage
                        );
            }
            if(selectedGods.size() == size) {
                godSelectorControl.selectGods(selectedGods);
            }
        }
    }

    private void chooseGod(String s) {
        availableGods.stream()
                .filter(g -> g.getName().toLowerCase().equals(s))
                .findFirst()
                .ifPresentOrElse(
                        godSelectorControl::chooseGod,
                        this::printUsage
                );
    }

    private void onWaitForChallenger() {
        CLI.clientInfo("Waiting for challenger to choose who should start");
    }

    private void chooseFirstPlayer(List<User> users) {
        inputHandler = s -> users.stream()
                .filter(u -> u.getUsername().toLowerCase().equals(s))
                .findFirst()
                .ifPresentOrElse(
                        godSelectorControl::chooseFirstPlayer,
                        this::userError
                        );
    }

    private void onGodsAvailable(List<GodIdentifier> godIdentifiers) {
        availableGods = godIdentifiers;
        CLI.clientInfo("Gods: " +
                availableGods.stream().map(GodIdentifier::getName)
                        .reduce("", (res, s) -> res + s + ", ")
                        .replace(",$","")
                + "\n");
        inputHandler = this::chooseGod;
    }

    private void printUsage() {
        System.out.println("Choose the gods in the list");
        System.out.flush();
    }

    private void userError() {
        CLI.clientInfo("User not found");
    }

}
