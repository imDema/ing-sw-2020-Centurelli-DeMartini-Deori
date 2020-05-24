package it.polimi.ingsw.view.client.gui;

import it.polimi.ingsw.view.client.ServerHandler;
import it.polimi.ingsw.view.client.gui.game.GameView;
import it.polimi.ingsw.view.client.gui.setup.ConnectionDialog;
import it.polimi.ingsw.view.client.gui.setup.GodSelectorView;
import it.polimi.ingsw.view.client.gui.setup.LoginView;
import it.polimi.ingsw.view.client.state.BoardViewModel;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class App extends Application {
    protected static ExecutorService executor = Executors.newCachedThreadPool();

    private ServerHandler server;
    private BoardViewModel boardViewModel;

    @Override
    public void start(Stage stage) {
        ConnectionDialog connectionDialog = new ConnectionDialog();

        Optional<ServerHandler> serverOpt = connectionDialog.showAndWait();
        serverOpt.ifPresentOrElse(
                s -> server = s,
                () -> System.exit(1));

        boardViewModel = new BoardViewModel();
        LoginView loginView = new LoginView(server, boardViewModel);
        executor.submit(server);

        server.dispatcher().setOnServerErrorListener((type, desc) -> Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(type);
            alert.setContentText(desc);
            alert.showAndWait();
            start(stage);
        }));

        stage.setTitle("Login");
        stage.setScene(new Scene(loginView));
        stage.show();

        server.dispatcher().setOnGodsAvailableListener(gods -> {
            GodSelectorView godSelector = new GodSelectorView(server, boardViewModel, gods);
            Platform.runLater(() -> {
                stage.setTitle("Choose your god");
                stage.setScene(new Scene(godSelector, 1200, 600));
            });
        });

        server.dispatcher().setOnRequestPlacePawnsListener(user -> {
            GameView gameView = new GameView(server, boardViewModel);
            Platform.runLater(() -> {
                stage.setTitle("Santorini");
                stage.setScene(new Scene(gameView, 1200, 600));
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
