package it.polimi.ingsw;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.InputStream;
import java.util.Optional;
import java.util.Scanner;

public abstract class Resources {
    public static String loadGodConfig(Object context) {
        InputStream config = context.getClass().getClassLoader().getResourceAsStream("config/gods.json");
        if (config != null) {
            Scanner scanner = new Scanner(config);
            scanner.useDelimiter(""); // Read to end
            StringBuilder sb = new StringBuilder();
            while(scanner.hasNext()){
                sb.append(scanner.next());
            }
            return sb.toString();
        } else {
            System.err.println("ERROR: Could not load \"config/gods.json\" from resources");
            return "";
        }
    }

    public static Optional<ImageView> loadGodCard(Object context, String godName) {
        String id = godName.strip().toLowerCase();
        InputStream stream = context.getClass().getClassLoader().getResourceAsStream("drawable/card_" + id + ".png");
        if (stream != null) {
            Image img = new Image(stream);
            return Optional.of(new ImageView(img));
        } else {
            return Optional.empty();
        }
    }

    // To be used as a fallback if loadGodCard failed
    public static ImageView loadGodCard(Object context) {
        InputStream stream = context.getClass().getClassLoader().getResourceAsStream("drawable/card_" + "apollo" + ".png"); // TODO replace
        if (stream != null) {
            Image img = new Image(stream);
            return new ImageView(img);
        } else {
            return new ImageView();
        }
    }

    public static ImageView loadBoardBackground(Object context) {
        InputStream stream = context.getClass().getClassLoader().getResourceAsStream("drawable/bg_board.png");
        if (stream != null) {
            Image img = new Image(stream);
            return new ImageView(img);
        } else {
            return new ImageView();
        }
    }
}
