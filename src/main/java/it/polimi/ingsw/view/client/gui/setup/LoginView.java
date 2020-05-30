package it.polimi.ingsw.view.client.gui.setup;

import it.polimi.ingsw.view.client.ServerHandler;
import it.polimi.ingsw.view.client.state.BoardViewModel;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

public class LoginView extends HBox {
    private final LoginViewModel loginViewModel;

    // Controls
    private final ComboBox<Integer> sizeComboBox = new ComboBox<>();
    private final TextField usernameTextField = new TextField();
    private final Button loginButton = new Button("Login");
    private final ListView<String> usersListView = new ListView<>();

    public LoginView(ServerHandler server, BoardViewModel boardViewModel) {
        loginViewModel = new LoginViewModel(server, boardViewModel);

        initView();
        bindViewModel();
    }

    private void bindViewModel() {
        usernameTextField.textProperty().bindBidirectional(loginViewModel.usernameProperty());
        loginViewModel.sizeProperty().bind(sizeComboBox.valueProperty());

        loginViewModel.setOnUserJoinedListener(u ->
                Platform.runLater(() -> usersListView.getItems().add(u.getUsername()))
        );

        loginViewModel.setOnSizeSetListener(size -> {
            sizeComboBox.setValue(size);
            sizeComboBox.setDisable(true);
        });
    }

    private void initView() {
        usersListView.setMinWidth(100);

        loginButton.setOnMouseClicked((mouseEvent) -> {
            if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                login();
            }
        });
        usernameTextField.setOnKeyPressed((keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                this.login();
            }
        }));

        sizeComboBox.getItems().addAll(1,2,3);
        sizeComboBox.setValue(1);

        GridPane gp = new GridPane();
        gp.add(new Label("Username"), 0, 0); gp.add(usernameTextField, 1, 0);
        gp.add(new Label("Game size"), 0, 1); gp.add(sizeComboBox, 1, 1);
        gp.add(loginButton, 0, 2);

        gp.setPadding(new Insets(20.0));

        this.setPadding(new Insets(10.0));
        this.getChildren().addAll(gp, usersListView);
    }

    private void login() {
        loginViewModel.setOnLoginAttempt((r, msg) -> {
            if (!r) {
                Platform.runLater(()->
                        new Alert(Alert.AlertType.INFORMATION, msg)
                                .showAndWait()
                );
            }
        });
        if (!sizeComboBox.isDisabled()) {
            loginViewModel.setSize();
        }
        loginViewModel.login();
    }
}
