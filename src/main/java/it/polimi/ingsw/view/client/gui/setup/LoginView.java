package it.polimi.ingsw.view.client.gui.setup;

import it.polimi.ingsw.view.client.ServerHandler;
import it.polimi.ingsw.view.client.state.BoardViewModel;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

public class LoginView extends HBox {
    private final LoginViewModel loginViewModel;

    // Controls
    private final Spinner<Integer> sizeSpinner = new Spinner<>(1,3, 1);
    private final TextField usernameTextField = new TextField();
    private final Button loginButton = new Button("Login");
    private final Button selectSizeButton = new Button("Select");
    private final ListView<String> usersListView = new ListView<>();

    public LoginView(ServerHandler server, BoardViewModel boardViewModel) {
        loginViewModel = new LoginViewModel(server, boardViewModel);

        initView();
        bindViewModel();
    }

    private void bindViewModel() {
        usernameTextField.textProperty().bindBidirectional(loginViewModel.usernameProperty());
        loginViewModel.sizeProperty().bind(sizeSpinner.valueProperty());

        loginViewModel.setOnUserJoinedListener(u ->
                Platform.runLater(() -> usersListView.getItems().add(u.getUsername()))
        );
    }

    private void initView() {
        usersListView.setMinWidth(100);

        loginButton.setOnMouseClicked(this::login);
        selectSizeButton.setOnMouseClicked(this::selectSize);

        GridPane gp = new GridPane();
        gp.add(new Label("Game size"), 0, 0);
        gp.add(sizeSpinner, 0, 1);
        gp.add(selectSizeButton, 0, 2);


        gp.add(new Label("Username"), 1, 0);
        gp.add(usernameTextField, 1, 1);
        gp.add(loginButton, 1, 2);


        gp.setPadding(new Insets(20.0));

        this.setPadding(new Insets(10.0));
        this.getChildren().addAll(gp, usersListView);
    }

    private void selectSize(MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == MouseButton.PRIMARY) {
            loginViewModel.setOnSetSizeAttempt((r, msg) -> {
                if (!r) {
                    Platform.runLater(()->
                        new Alert(Alert.AlertType.INFORMATION, msg)
                                .showAndWait()
                    );
                }
                selectSizeButton.setDisable(true);
                sizeSpinner.setDisable(true);
            });
            loginViewModel.setSize();
        }
    }

    private void login(MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == MouseButton.PRIMARY) {
            loginViewModel.setOnLoginAttempt((r, msg) -> {
                if (!r) {
                    Platform.runLater(()->
                            new Alert(Alert.AlertType.INFORMATION, msg)
                            .showAndWait()
                    );
                }
            });
            loginViewModel.login();
        }
    }
}
