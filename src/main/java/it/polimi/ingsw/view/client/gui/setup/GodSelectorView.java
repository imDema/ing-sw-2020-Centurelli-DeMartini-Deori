package it.polimi.ingsw.view.client.gui.setup;

import it.polimi.ingsw.Resources;
import it.polimi.ingsw.controller.messages.GodIdentifier;
import it.polimi.ingsw.view.client.ServerHandler;
import it.polimi.ingsw.view.client.state.BoardViewModel;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.FlowPane;

import java.util.ArrayList;
import java.util.List;

public class GodSelectorView extends FlowPane {
    private final double baseHeight = 270.0;

    private final GodSelectorViewModel godSelectorViewModel;

    List<Button> buttons = new ArrayList<>();

    public GodSelectorView(ServerHandler server, BoardViewModel boardViewModel, List<GodIdentifier> gods) {
        godSelectorViewModel = new GodSelectorViewModel(server, boardViewModel);
        generateButtons(gods);
        bindEvents();
        this.setPrefSize(600,300);
    }

    private void bindEvents() {
        godSelectorViewModel.setOnGodsAvailable(g -> Platform.runLater(() -> generateButtons(g)));
    }

    public void generateButtons(List<GodIdentifier> gods) {
        this.getChildren().clear();
        buttons.clear();

        gods.forEach(god -> {
            Button b = new Button();

            // Load image
            Resources.loadGodCard(this, god.getName())
                        .map(img -> {
                            img.setPreserveRatio(true);
                            img.setFitHeight(baseHeight);
                            return img;
                        })
                        .ifPresentOrElse(b::setGraphic, () -> b.setText(god.getName()));

            b.setOnMouseClicked(e -> {
                if (e.getButton() == MouseButton.PRIMARY) {
                    godSelectorViewModel.chooseGod(god);
                }
            });

            buttons.add(b);
            this.getChildren().add(b);
        });
    }
}
