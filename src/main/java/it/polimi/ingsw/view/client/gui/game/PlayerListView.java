package it.polimi.ingsw.view.client.gui.game;

import it.polimi.ingsw.Resources;
import it.polimi.ingsw.controller.messages.GodIdentifier;
import it.polimi.ingsw.controller.messages.User;
import it.polimi.ingsw.view.client.state.PlayerViewModel;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;


public class PlayerListView extends VBox {
    private final double cardBaseHeight = 200.0;

    private final GameViewModel gameViewModel;
    private final List<Label> labels = new ArrayList<>();


    public PlayerListView(GameViewModel gameViewModel) {
        this.gameViewModel = gameViewModel;
        initView();
        bindViewModel();
    }

    private void initView() {
        for (PlayerViewModel p : gameViewModel.getBoardViewModel().getPlayers()) {
            this.getChildren().add(playerView(p.getUser(), p.getGod()));
        }
    }

    private void bindViewModel() {
        gameViewModel.currentUserProperty().addListener((o,oldV,newV) -> highlight(newV));
    }

    private void highlight(User user) {
        labels.stream()
                .peek(l -> l.setStyle("-fx-underline: false;"))
                .filter(l -> l.getText().equals(user.getUsername()))
                .forEach(l -> l.setStyle("-fx-underline: true;"));
    }

    private Node playerView(User user, GodIdentifier god) {
        ImageView godView = Resources.loadGodCard(this, god.getName())
                .orElse(Resources.loadGodCard(this));
        godView.setPreserveRatio(true);
        godView.setFitHeight(cardBaseHeight);

        Label playerName = new Label(user.getUsername());
        labels.add(playerName); // TODO: Change this

        VBox playerView = new VBox(godView, playerName);

        playerView.setAlignment(Pos.CENTER);
        playerView.setPadding(new Insets(10.0));
        return playerView;
    }
}
