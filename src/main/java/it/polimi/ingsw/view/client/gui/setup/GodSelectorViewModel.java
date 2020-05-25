package it.polimi.ingsw.view.client.gui.setup;

import it.polimi.ingsw.controller.messages.GodIdentifier;
import it.polimi.ingsw.controller.messages.User;
import it.polimi.ingsw.view.client.ServerHandler;
import it.polimi.ingsw.view.client.gui.game.PlayerListView;
import it.polimi.ingsw.view.client.state.BoardViewModel;
import javafx.application.Platform;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.scene.control.Alert;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;


public class GodSelectorViewModel {
    private final ServerHandler server;
    private final BoardViewModel boardViewModel;
    private final ListProperty<PlayerListView> players = new SimpleListProperty<>(); // TODO: temp mockup

    private Consumer<List<GodIdentifier>> godsAvailableListener = null;

    public ListProperty<PlayerListView> playersProperty() {
        return players;
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
        if (godsAvailableListener != null) {
            godsAvailableListener.accept(godIdentifiers);
        }
    }

    private void onGodChosen(User user, GodIdentifier godIdentifier) {
        boardViewModel.getPlayer(user).ifPresent(u -> u.setGod(godIdentifier));
    }

    public void chooseGod(GodIdentifier godId) {
        Optional<User> user = boardViewModel.getMyUser(); // Should never happen
        if (user.isPresent()) {
            server.onChooseGod(user.get(), godId);
        } else {
            Platform.runLater(() -> new Alert(Alert.AlertType.INFORMATION, "Spectators can't choose gods!"));
        }
    }
}
