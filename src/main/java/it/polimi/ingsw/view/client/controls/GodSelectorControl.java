package it.polimi.ingsw.view.client.controls;

import it.polimi.ingsw.controller.messages.GodIdentifier;
import it.polimi.ingsw.controller.messages.User;
import it.polimi.ingsw.view.client.ServerHandler;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Client side controller for the god selection phase of the game
 */
public class GodSelectorControl {
    private final ServerHandler server;
    private final BoardViewState boardViewState;

    private Consumer<List<GodIdentifier>> godsAvailableListener = null;
    private Consumer<List<User>> chooseFirstPlayerListener = null;
    private Runnable waitForChallengerListener = null;

    public GodSelectorControl(ServerHandler server, BoardViewState boardViewState) {
        this.server = server;
        this.boardViewState = boardViewState;
        server.dispatcher().setOnGodChosenListener(this::onGodChosen);
        server.dispatcher().setOnGodsAvailableListener(this::onGodsAvailable);
    }

    public void setOnChooseFirstPlayer(Consumer<List<User>> chooseFirstPlayerListener) {
        this.chooseFirstPlayerListener = chooseFirstPlayerListener;
    }

    public void setOnWaitForChallenger(Runnable waitForChallengerListener) {
        this.waitForChallengerListener = waitForChallengerListener;
    }

    public void setOnGodsAvailable(Consumer<List<GodIdentifier>> godsAvailableListener) {
        this.godsAvailableListener = godsAvailableListener;
    }

    public int getLobbySize() {
        return boardViewState.getPlayers().size();
    }

    private void onGodsAvailable(List<GodIdentifier> godIdentifiers) {
        if (godIdentifiers.size() > 0 && godsAvailableListener != null) {
            godsAvailableListener.accept(godIdentifiers);
        } else {
            if (boardViewState.isChallenger() && chooseFirstPlayerListener != null) {
                User myUser = boardViewState.getMyUser().orElseThrow();
                List<User> others = boardViewState.getPlayers().stream()
                        .map(PlayerViewState::getUser)
                        .filter(user -> !user.equals(myUser))
                        .collect(Collectors.toList());
                if (others.size() >= 2) {
                    chooseFirstPlayerListener.accept(others);
                } else if (others.size() == 1) {
                    chooseFirstPlayer(others.get(0));
                } else {
                    chooseFirstPlayer(myUser);
                }
            } else if (waitForChallengerListener != null){
                waitForChallengerListener.run();
            }
        }
    }

    private void onGodChosen(User user, GodIdentifier godIdentifier) {
        boardViewState.getPlayer(user).ifPresent(u -> u.setGod(godIdentifier));
    }

    /**
     * Request the selection of available gods
     * @param selectedGods list of gods to select
     * @return true if the request was sent, false otherwise (if the client is not logged in)
     */
    public boolean selectGods(List<GodIdentifier> selectedGods) {
        Optional<User> user = boardViewState.getMyUser();
        if (user.isPresent()) {
            server.dispatcher().setOnResultListener(r -> {
                if (r) {
                    boardViewState.setIsChallenger(true);
                }
            });
            server.onSelectGods(user.get(), selectedGods);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Request the choice of a god for this user
     * @param godId god to choose
     * @return true if the request was sent, false otherwise (if the client is not logged in)
     */
    public boolean chooseGod(GodIdentifier godId) {
        Optional<User> user = boardViewState.getMyUser();
        if (user.isPresent()) {
            server.onChooseGod(user.get(), godId);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Request the choice of the first player to start placing pawns and moving
     * @param user user that should start first
     * @return true if the request was sent, false otherwise (if the client is not logged in)
     */
    public boolean chooseFirstPlayer(User user) {
        Optional<User> self = boardViewState.getMyUser();
        if (self.isPresent()) {
            server.onChooseFirstPlayer(self.get(), user);
            return true;
        } else {
            return false;
        }
    }
}
