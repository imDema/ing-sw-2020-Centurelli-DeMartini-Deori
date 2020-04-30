package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.serialization.Serializer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

public class GodFactory {
    private final String PATH_GODS = "src/main/resources/gods.json";

    public List<God> getGods() throws IOException {
        String resource = readConfig();
        return Serializer.getGodArray(resource);
    }

    private String readConfig() throws IOException {
        Stream<String> lines = Files.lines(Paths.get(PATH_GODS));
        return lines.reduce("", (str, sub) -> str + sub);
    }
}
