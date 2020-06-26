package it.polimi.ingsw.view.client.gui.setup;

import it.polimi.ingsw.view.client.ProxyController;
import it.polimi.ingsw.view.client.ServerHandler;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.io.IOException;

/**
 * Dialog used to connect to a server and supply a {@link ServerHandler} that will be used to communicate
 */
public class ConnectionDialog extends Dialog<ServerHandler> {
    private final TextField ipTextField = new TextField("ec2-34-224-16-61.compute-1.amazonaws.com");
    private final TextField portTextField = new TextField("5656");

    public ConnectionDialog() {
        VBox vbox = new VBox(
                new Label("Ip:"), ipTextField,
                new Label("Port:"), portTextField
        );

        vbox.setSpacing(10.0);
        vbox.setPadding(new Insets(40.0));

        DialogPane dp = getDialogPane();

        setTitle("Connect to server");
        setResultConverter(this::connectConverter);

        ButtonType bt = new ButtonType("Connect", ButtonBar.ButtonData.OK_DONE);
        dp.getButtonTypes().addAll(bt, ButtonType.CANCEL);
        dp.setContent(vbox);
    }

    private ServerHandler connectConverter(ButtonType bt) {
        ServerHandler result = null;
        if (bt.getButtonData() == ButtonBar.ButtonData.OK_DONE) {
            try {
                int port = Integer.parseInt(portTextField.getText());
                ProxyController controller = new ProxyController(ipTextField.getText(), port);
                try {
                    result = controller.start();
                } catch (IOException e) {
                    new Alert(Alert.AlertType.ERROR, "Error connecting to server.\nDetails:\n" + e.getMessage())
                            .showAndWait();
                }
            } catch (NumberFormatException e) {
                new Alert(Alert.AlertType.ERROR, "Error invalid number format for port")
                        .showAndWait();
            }
        }
        return result;
    }
}
