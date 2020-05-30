package it.polimi.ingsw.view.client.gui.setup;

import it.polimi.ingsw.controller.messages.GodIdentifier;
import it.polimi.ingsw.controller.messages.User;
import it.polimi.ingsw.view.client.ServerHandler;
import it.polimi.ingsw.view.client.state.BoardViewModel;
import it.polimi.ingsw.view.client.state.PlayerViewModel;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;


public class GodSelectorViewModel {
    private final ServerHandler server;
    private final BoardViewModel boardViewModel;

    private Consumer<List<GodIdentifier>> godsAvailableListener = null;
    private Consumer<List<User>> chooseFirstPlayerListener = null;
    private Runnable waitForChallengerListener = null;

    public int getLobbySize() {
        return boardViewModel.getPlayers().size();
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

    public GodSelectorViewModel(ServerHandler server, BoardViewModel boardViewModel) {
        this.server = server;
        this.boardViewModel = boardViewModel;
        server.dispatcher().setOnGodChosenListener(this::onGodChosen);
        server.dispatcher().setOnGodsAvailableListener(this::onGodsAvailable);
    }

    private void onGodsAvailable(List<GodIdentifier> godIdentifiers) {
        if (godIdentifiers.size() > 0 && godsAvailableListener != null) {
            godsAvailableListener.accept(godIdentifiers);
        } else {
            if (boardViewModel.isChallenger() && chooseFirstPlayerListener != null) {
                User myUser = boardViewModel.getMyUser().orElseThrow();
                List<User> others = boardViewModel.getPlayers().stream()
                        .map(PlayerViewModel::getUser)
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
        boardViewModel.getPlayer(user).ifPresent(u -> u.setGod(godIdentifier));
    }

    public boolean selectGods(List<GodIdentifier> selectedGods) {
        Optional<User> user = boardViewModel.getMyUser();
        if (user.isPresent()) {
            server.dispatcher().setOnResultListener(r -> {
                if (r) {
                    boardViewModel.setIsChallenger(true);
                }
            });
            server.onSelectGods(user.get(), selectedGods);
            return true;
        } else {
            return false;
        }
    }

    public boolean chooseGod(GodIdentifier godId) {
        Optional<User> user = boardViewModel.getMyUser();
        if (user.isPresent()) {
            server.onChooseGod(user.get(), godId);
            return true;
        } else {
            return false;
        }
    }

    public boolean chooseFirstPlayer(User user) {
        Optional<User> self = boardViewModel.getMyUser();
        if (self.isPresent()) {
            server.onChooseFirstPlayer(self.get(), user);
            return true;
        } else {
            return false;
        }
    }
}
