package it.polimi.ingsw.view.client.gui.setup;

import it.polimi.ingsw.Resources;
import it.polimi.ingsw.controller.messages.GodIdentifier;
import it.polimi.ingsw.controller.messages.User;
import it.polimi.ingsw.view.client.ServerHandler;
import it.polimi.ingsw.view.client.state.BoardViewModel;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.FlowPane;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GodSelectorView extends FlowPane {
    private final double baseHeight = 270.0;
    private final List<GodIdentifier> selectedGods = new ArrayList<>();

    private final GodSelectorViewModel godSelectorViewModel;

    List<Button> buttons = new ArrayList<>();

    public GodSelectorView(ServerHandler server, BoardViewModel boardViewModel, List<GodIdentifier> gods) {
        godSelectorViewModel = new GodSelectorViewModel(server, boardViewModel);
        generateSelectButtons(gods);
        bindEvents();
        this.setPrefSize(600,300);
    }

    private void bindEvents() {
        godSelectorViewModel.setOnGodsAvailable(g -> Platform.runLater(() -> generateChooseButtons(g)));
        godSelectorViewModel.setOnChooseFirstPlayer(this::chooseFirstPlayer);
        godSelectorViewModel.setOnWaitForChallenger(this::waitForChallenger);
    }

    private void waitForChallenger() {
        Platform.runLater(() -> {
            this.getChildren().clear();
            this.getChildren().add(new Label("Waiting for challenger to choose who should start"));
        });
    }

    private void chooseFirstPlayer(List<User> users) {
        Platform.runLater(() -> {
            this.getChildren().clear();
            Optional<User> first;
            do {
                first = Optional.empty();
                while (first.isEmpty()) {
                    ChooseFirstDialog dialog = new ChooseFirstDialog(users);
                    first = dialog.showAndWait();
                }
            } while (!godSelectorViewModel.chooseFirstPlayer(first.get()));
        });
    }

    private void generateSelectButtons(List<GodIdentifier> gods) {
        gods.forEach(god -> {
            Button b = godButton(god);
            b.setOnMouseClicked(e -> {
                if (e.getButton() == MouseButton.PRIMARY) {
                    selectedGods.add(god);
                    b.setDisable(true);
                    if (selectedGods.size() == godSelectorViewModel.getLobbySize()) {
                        boolean res = godSelectorViewModel.selectGods(selectedGods);
                        if (!res) Platform.runLater(() -> {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Cannot select gods");
                            alert.showAndWait();
                        });
                    }

                }
            });

            buttons.add(b);
            this.getChildren().add(b);
        });
    }

    public void generateChooseButtons(List<GodIdentifier> gods) {
        this.getChildren().clear();
        buttons.clear();

        gods.forEach(god -> {
            Button b = godButton(god);
            b.setOnMouseClicked(e -> {
                if (e.getButton() == MouseButton.PRIMARY) {
                    boolean res = godSelectorViewModel.chooseGod(god);
                    if (!res) Platform.runLater(() -> {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Spectators can't choose gods!");
                        alert.showAndWait();
                    });
                }
            });

            buttons.add(b);
            this.getChildren().add(b);
        });
    }

    private Button godButton(GodIdentifier god) {
        Button b = new Button();

        // Load image
        Resources.loadGodCard(this, god.getName())
                .map(img -> {
                    img.setPreserveRatio(true);
                    img.setFitHeight(baseHeight);
                    return img;
                })
                .ifPresentOrElse(b::setGraphic, () -> b.setText(god.getName()));
        return b;
    }
}
