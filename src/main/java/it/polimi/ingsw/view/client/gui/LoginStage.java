package it.polimi.ingsw.view.client.gui;

import it.polimi.ingsw.controller.messages.User;
import it.polimi.ingsw.view.client.ServerHandler;
import it.polimi.ingsw.view.client.state.BoardView;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class LoginStage extends Stage {
    private final ServerHandler server;
    private final BoardView board = new BoardView();
    private final List<User> users = new ArrayList<>();

    // Controls
    private final BorderPane root = new BorderPane();
    private final Spinner<Integer> sizeSpinner = new Spinner<>(2,3, 3);
    private final TextField usernameTextField = new TextField();
    private final Button loginButton = new Button();
    private final ListView<String> usersListView = new ListView<>();

    // Listeners

    public LoginStage (ServerHandler server) {
        super();
        this.server = server;
        server.setOnUserJoinedListener(u -> {
            users.add(u);
            usersListView.getItems().add(u.getUsername());
        });

        usersListView.setMinWidth(100);

        loginButton.setOnMouseClicked(this::onLoginButtonClick);
        usernameTextField.setOnKeyReleased(this::onUsernameKeyReleased);
        sizeSpinner.setOnKeyReleased(this::onSpinnerKeyReleased);


        VBox contentVBox = new VBox();
        contentVBox.getChildren().addAll(
            new Label("Game size"),
            sizeSpinner,
            new Label("Choose an username"),
            usernameTextField
        );

        root.setCenter(contentVBox);
        root.setRight(usersListView);

        setMinHeight(200);
        setMinWidth(300);
        setTitle("Connect to server");
        setScene(new Scene(root));

        setTitle("Login");
    }

    private void onSpinnerKeyReleased(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            int size = sizeSpinner.getValue();

            server.onSelectPlayerNumber(size);

            server.setOnResultListener(r -> {
                sizeSpinner.setDisable(true);
                sizeSpinner.setOnKeyReleased(null);
                if (!r) {
                    new Alert(Alert.AlertType.INFORMATION, "Game size was already set")
                            .showAndWait();
                }
            });
        }
    }

    private void onUsernameKeyReleased(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            String username = usernameTextField.getCharacters().toString();
            server.onAddUser(new User(username));

            usersListView.getItems()
                .add(username);
            usernameTextField.setText("");
        }
    }

    private void onLoginButtonClick(MouseEvent mouseEvent) {

    }

    @Override
    public void close() {
        super.close();
    }
}
