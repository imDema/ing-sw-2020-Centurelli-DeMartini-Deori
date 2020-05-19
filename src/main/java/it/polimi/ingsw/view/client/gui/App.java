package it.polimi.ingsw.view.client.gui;

import it.polimi.ingsw.view.client.ServerHandler;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class App extends Application {
    ServerHandler server;
    protected static ExecutorService executor = Executors.newCachedThreadPool();

    ConnectionDialog connectionDialog;
    LoginView loginView;

    @Override
    public void start(Stage stage) {
        connectionDialog = new ConnectionDialog();

        Optional<ServerHandler> serverOpt = connectionDialog.showAndWait();
        serverOpt.ifPresentOrElse(
                s -> server = s,
                () -> System.exit(1));

        loginView = new LoginView(server);
        executor.submit(server);

        stage.setTitle("Login");
        stage.setScene(new Scene(loginView));
        stage.show();

        server.setOnGodsAvailableListener(gods -> {
            GodSelectorView godSelector = new GodSelectorView(gods, server);
            Platform.runLater(() -> {
                stage.setTitle("Choose your god");
                stage.setScene(new Scene(godSelector, 1200, 600));
            });
        });

        server.setOnRequestPlacePawnsListener(user -> {

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
