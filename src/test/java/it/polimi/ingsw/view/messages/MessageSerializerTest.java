package it.polimi.ingsw.view.messages;

import it.polimi.ingsw.controller.messages.ActionIdentifier;
import it.polimi.ingsw.controller.messages.GodIdentifier;
import it.polimi.ingsw.controller.messages.User;
import it.polimi.ingsw.model.action.*;
import it.polimi.ingsw.model.board.Building;
import it.polimi.ingsw.model.board.Coordinate;
import it.polimi.ingsw.model.player.God;
import it.polimi.ingsw.serialization.Serializer;
import org.junit.jupiter.api.Test;

import javax.sound.midi.SysexMessage;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MessageSerializerTest {

    @Test
    public void testSerialize() {
        final String name1 = "user 1";
        final AddUserMessage msg1 = new AddUserMessage(name1);

        final User user2 = new User("user 2");
        final GodIdentifier god2 = new GodIdentifier(new God("God 2", "Description", null));
        final ChooseGodMessage msg2 = new ChooseGodMessage(user2, god2);

        String json1 = assertDoesNotThrow(() -> Serializer.serializeMessage(msg1));
        String json2 = assertDoesNotThrow(() -> Serializer.serializeMessage(msg2));

        //System.out.println(json1);  // {"type":"ADD_USER","content":{"user":{"username":"user 1"}}}
        //System.out.println(json2);  // {"type":"CHOOSE_GOD","content":{"user":{"username":"user 2"},"god":{"name":"God 2","description":"Description"}}}


        Message des1 = assertDoesNotThrow(() -> Serializer.deserializeMessage(json1));
        Message des2 = assertDoesNotThrow(() -> Serializer.deserializeMessage(json2));

        assertEquals(msg1.getSerializationId(), des1.getSerializationId());
        assertEquals(msg2.getSerializationId(), des2.getSerializationId());

        assertTrue(des1 instanceof AddUserMessage);
        assertTrue(des2 instanceof ChooseGodMessage);

        AddUserMessage cast1 = (AddUserMessage) des1;
        ChooseGodMessage cast2 = (ChooseGodMessage) des2;

        assertEquals(msg1.getUser(), cast1.getUser());
        assertEquals(msg2.getUser(), cast2.getUser());
        assertEquals(msg2.getGod().getName(), cast2.getGod().getName());
    }
}