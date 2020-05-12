package it.polimi.ingsw;

import java.io.InputStream;
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
}
