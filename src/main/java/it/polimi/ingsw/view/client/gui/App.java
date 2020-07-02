package it.polimi.ingsw.view.client.gui;

import it.polimi.ingsw.view.client.ServerHandler;
import it.polimi.ingsw.view.client.controls.BoardViewState;
import it.polimi.ingsw.view.client.gui.game.GameView;
import it.polimi.ingsw.view.client.gui.setup.ConnectionDialog;
import it.polimi.ingsw.view.client.gui.setup.GodSelectorView;
import it.polimi.ingsw.view.client.gui.setup.LoginView;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;

import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Main client class for the graphic client
 */
public class App extends Application {
    protected static final ExecutorService executor = Executors.newCachedThreadPool();

    private ServerHandler server;
    private BoardViewState boardViewState;

    @Override
    public void start(Stage stage) {
        ConnectionDialog connectionDialog = new ConnectionDialog();

        Optional<ServerHandler> serverOpt = connectionDialog.showAndWait();
        serverOpt.ifPresentOrElse(
                s -> server = s,
                () -> System.exit(1));

        boardViewState = new BoardViewState();
        LoginView loginView = new LoginView(server, boardViewState);
        executor.submit(server);

        server.dispatcher().setOnServerErrorListener((type, desc) -> Platform.runLater(() -> {
            server.stop();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(type);
            alert.setContentText(desc);
            stage.hide();
            alert.showAndWait();
            start(stage);
        }));

        stage.setTitle("Login");
        stage.setScene(new Scene(loginView));
        stage.show();

        server.dispatcher().setOnGodsAvailableListener(gods -> {
            GodSelectorView godSelector = new GodSelectorView(server, boardViewState, gods);
            Platform.runLater(() -> {
                ScrollPane scrollPane = new ScrollPane();
                scrollPane.setFitToHeight(true);
                scrollPane.setFitToWidth(true);
                scrollPane.setContent(godSelector);

                stage.setTitle("Choose the gods if you are worthy to be the challenger!");
                stage.setScene(new Scene(scrollPane, 820, 540));
                stage.setMinWidth(828);
                stage.setMinHeight(580);
            });
        });

        server.dispatcher().setOnRequestPlacePawnsListener(firstUser -> {
            GameView gameView = new GameView(server, boardViewState, firstUser);
            Scene scene = new Scene(gameView, 960, 540);

            Platform.runLater(() -> {
                stage.setTitle("Santorini");
                stage.setScene(scene);
                stage.setMinHeight(400);
                stage.setMinWidth(600);
            });
        });

        server.dispatcher().setOnWinListener(user -> {
            server.dispatcher().setOnServerErrorListener(null);
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText(user.getUsername() + " won! Congratulations!");
                alert.showAndWait();
                stage.hide();
                start(stage);
            });
        });
    }

    @Override
    public void stop() throws Exception {
        server.stop();
        executor.shutdown();
        super.stop();
    }

    public static void main() {
        Application.launch();
    }
}
