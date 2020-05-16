package it.polimi.ingsw.view.client.gui;

import it.polimi.ingsw.view.client.ProxyController;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class GUIClient extends Application {
    ProxyController controller;
    ExecutorService executor = Executors.newCachedThreadPool();

    ConnectionStage connectionStage;
    LoginStage loginStage;

    @Override
    public void start(Stage stage) {
        connectionStage = new ConnectionStage();
        connectionStage.setOnConnectListener(server -> {
            loginStage = new LoginStage(server);
            connectionStage.close();
            executor.submit(server);
            loginStage.show();
        });

        connectionStage.showAndWait();
    }

    public static void main() {
        Application.launch();
    }
}
