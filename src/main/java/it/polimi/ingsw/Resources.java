package it.polimi.ingsw;

import it.polimi.ingsw.model.board.Building;
import it.polimi.ingsw.view.cli.CLI;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Optional;
import java.util.Scanner;

public abstract class Resources {
    private static String godConfigPath = null;
    private static boolean customConfig = false;

    public static void setGodConfigFile(String path) {
        if (path != null) {
            godConfigPath = path;
            customConfig = true;
        } else {
            godConfigPath = null;
            customConfig = false;
        }
    }

    public static boolean usingCustomConfig() {
        return customConfig;
    }

    public static String loadGodConfig(Object context) {
        InputStream config;
        if (customConfig) {
            try{
                config = new FileInputStream(godConfigPath);
            } catch (FileNotFoundException e) {
                CLI.error("File not found: \"" + godConfigPath + "\" falling back to default configuration");
                config = context.getClass().getClassLoader().getResourceAsStream("config/gods.json");
            }
        } else {
            config = context.getClass().getClassLoader().getResourceAsStream("config/gods.json");
        }


        if (config != null) {
            Scanner scanner = new Scanner(config);
            scanner.useDelimiter(""); // Read to end
            StringBuilder sb = new StringBuilder();
            while(scanner.hasNext()){
                sb.append(scanner.next());
            }
            return sb.toString();
        } else {
            CLI.error("ERROR: Could not load god configuration");
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
        return loadImage(context, "drawable/card_" + "apollo" + ".png");
    }

    public static ImageView loadBoardBackground(Object context) {
        return loadImage(context, "drawable/bg_board_sea.png");
    }

    public static ImageView loadBoardForeground(Object context) {
        return loadImage(context, "drawable/bg_board_transparent.png");
    }

    public static ImageView loadBuilding(Object context, Building building) {
        if (building.hasDome()) {
            return loadImage(context, "drawable/cell_dome.png");
        } else {
            return switch (building.getLevel()) {
                case LEVEL0 -> new ImageView();
                case LEVEL1 -> loadImage(context, "drawable/cell_l1.png");
                case LEVEL2 -> loadImage(context, "drawable/cell_l2.png");
                case LEVEL3 -> loadImage(context, "drawable/cell_l3.png");
            };
        }
    }

    public static Optional<Image> loadPawn(Object context, int id) {
        InputStream stream = context.getClass().getClassLoader().getResourceAsStream("drawable/pawn_" + id + ".png");
        if (stream != null) {
            Image img = new Image(stream);
            return Optional.of(img);
        } else {
            return Optional.empty();
        }
    }

    public static ImageView loadCellHighlight(Object context) {
        return loadImage(context, "drawable/highlight_0.png");
    }

    private static ImageView loadImage(Object context, String path) {
        InputStream stream = context.getClass().getClassLoader().getResourceAsStream(path);
        if (stream != null) {
            Image img = new Image(stream);
            return new ImageView(img);
        } else {
            throw new IllegalStateException(); //TODO remove in production
            // return new ImageView();
        }
    }
}
