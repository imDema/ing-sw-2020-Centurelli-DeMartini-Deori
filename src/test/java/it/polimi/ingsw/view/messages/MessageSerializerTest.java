package it.polimi.ingsw.view.messages;

import it.polimi.ingsw.controller.messages.GodIdentifier;
import it.polimi.ingsw.controller.messages.User;
import it.polimi.ingsw.model.player.God;
import it.polimi.ingsw.serialization.Serializer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MessageSerializerTest {

    @Test
    void serialize() {
        final String name1 = "user 1";
        final AddUserMessage msg1 = new AddUserMessage(name1);

        final User user2 = new User("user 2");
        final GodIdentifier god2 = new GodIdentifier(new God("God 2", "Description", null));
        final ChooseGodMessage msg2 = new ChooseGodMessage(user2, god2);

        String json1 = assertDoesNotThrow(() -> Serializer.serializeMessage(msg1));
        String json2 = assertDoesNotThrow(() -> Serializer.serializeMessage(msg2));

        System.out.println(json1);
        System.out.println(json2);

        Message des1 = assertDoesNotThrow(() -> Serializer.deserializeMessage(json1));
        Message des2 = assertDoesNotThrow(() -> Serializer.deserializeMessage(json2));

        assertEquals(msg1.getSerializationId(), des1.getSerializationId());
        assertEquals(msg2.getSerializationId(), des2.getSerializationId());

        assertTrue(des1 instanceof AddUserMessage);
        assertTrue(des2 instanceof ChooseGodMessage);

        AddUserMessage cast1 = (AddUserMessage) des1;
        ChooseGodMessage cast2 = (ChooseGodMessage) des2;

        assertEquals(msg1.getName(), cast1.getName());
        assertEquals(msg2.getUser(), cast2.getUser());
        assertEquals(msg2.getGod().getName(), cast2.getGod().getName());
    }
}