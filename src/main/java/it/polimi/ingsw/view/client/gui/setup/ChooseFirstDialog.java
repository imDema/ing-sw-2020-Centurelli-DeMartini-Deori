package it.polimi.ingsw.view.client.gui.setup;

import it.polimi.ingsw.controller.messages.User;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.util.List;

public class ChooseFirstDialog extends Dialog<User> {
    private final ComboBox<User> comboBox = new ComboBox<>();

    public ChooseFirstDialog(List<User> users) {
        comboBox.getItems().addAll(users);
        comboBox.setValue(users.get(0));
        comboBox.setCellFactory(pairListView -> new ListCell<>() {
            @Override
            protected void updateItem(User item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText("");
                } else {
                    setText(item.getUsername());
                }
            }
        });
        comboBox.setButtonCell(null);

        VBox vbox = new VBox(
                new Label("Who should start?"),
                comboBox
        );

        vbox.setSpacing(10.0);
        vbox.setPadding(new Insets(40.0));

        DialogPane dp = getDialogPane();

        setTitle("Choose who should start first");
        setResultConverter(this::converter);

        ButtonType bt = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
        dp.getButtonTypes().addAll(bt);
        dp.setContent(vbox);
    }

    private User converter(ButtonType bt) {
        User user = null;
        if (bt.getButtonData() == ButtonBar.ButtonData.OK_DONE) {
            user = comboBox.getValue();
        }
        return user;
    }
}
