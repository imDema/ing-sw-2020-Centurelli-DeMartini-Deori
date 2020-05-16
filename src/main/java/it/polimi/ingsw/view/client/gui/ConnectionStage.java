package it.polimi.ingsw.view.client.gui;

import it.polimi.ingsw.view.client.ProxyController;
import it.polimi.ingsw.view.client.ServerHandler;
import it.polimi.ingsw.view.client.gui.events.OnConnectListener;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;


public class ConnectionStage extends Stage {
    // Controls
    private final BorderPane root = new BorderPane();
    private final TextField ipTextField = new TextField("127.0.0.1");
    private final TextField portTextField = new TextField("5000");
    private final Button connectButton = new Button("Connect");

    // Listeners
    private OnConnectListener onConnectListener;

    public void setOnConnectListener(OnConnectListener onConnectListener) {
        this.onConnectListener = onConnectListener;
    }

    public ConnectionStage() {
        super();
        connectButton.setOnMouseClicked(this::onConnectButtonClicked);

        VBox contentVBox = new VBox();
        contentVBox.getChildren().addAll(
                new Label("Ip:"),
                ipTextField,
                new Label("Port:"),
                portTextField,
                connectButton
        );

        root.setCenter(contentVBox);

        setMinHeight(200);
        setMinWidth(300);
        setTitle("Connect to server");
        setScene(new Scene(root));
    }

    private void onConnectButtonClicked(MouseEvent mouseEvent) {
        try {
            String ip = ipTextField.getCharacters().toString();
            int port = Integer.parseInt(portTextField.getCharacters().toString());
            ProxyController controller = new ProxyController(ip, port);

            try {
                ServerHandler handler = controller.start();
                if (onConnectListener != null) {
                    onConnectListener.onConnect(handler);
                }
            } catch (IOException e) {
                new Alert(Alert.AlertType.ERROR, "Error connecting to server.\nDetails:\n" + e.getMessage())
                        .showAndWait();
            }


        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "Error invalid number format for port")
                    .showAndWait();
        }
    }
}
