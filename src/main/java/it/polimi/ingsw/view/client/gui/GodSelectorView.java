package it.polimi.ingsw.view.client.gui;

import it.polimi.ingsw.Resources;
import it.polimi.ingsw.controller.messages.GodIdentifier;
import it.polimi.ingsw.view.client.ServerHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.FlowPane;

import java.util.ArrayList;
import java.util.List;

public class GodSelectorView extends FlowPane {
    private final double ratio = 1410.0 / 840.0;
    private final double baseHeight = 300.0;
    private final double baseWidth = baseHeight / ratio;

    private final GodSelectorViewModel godSelectorViewModel;

    List<Button> buttons = new ArrayList<>();

    public GodSelectorView(List<GodIdentifier> gods, ServerHandler server) {
        godSelectorViewModel = new GodSelectorViewModel(server);
        generateButtons(gods);
        bindEvents();
        this.setPrefSize(600,300);
    }

    private void bindEvents() {

    }

    public void generateButtons(List<GodIdentifier> gods) {
        this.getChildren().removeAll();
        buttons.clear();

        gods.forEach(god -> {
            Button b = new Button(god.getName());

            // Load image
            Resources.loadGodCard(this, god.getName())
                        .map(img -> {
                            img.setFitWidth(baseWidth);
                            img.setFitHeight(baseHeight);
                            return img;
                        })
                        .ifPresent(b::setGraphic);


            b.setPrefSize(baseWidth, baseHeight);

            b.setOnMouseClicked(e -> {
                if (e.getButton() == MouseButton.PRIMARY) {
                    select(god);
                }
            });

            buttons.add(b);
            this.getChildren().add(b);
        });
    }

    private void select(GodIdentifier god) {
        new Alert(Alert.AlertType.INFORMATION, "Chose god " + god.getName())
            .showAndWait();
    }
}
