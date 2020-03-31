package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.action.ActionKind;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DefaultTurnSequenceTest {

    @Test
    public void testGetStep() {
         TurnSequence t = new DefaultTurnSequence();
         assertEquals(t.getStep(), List.of(new ActionKind[]{ActionKind.MOVE, ActionKind.MOVE_UP}));

         t.nextStep(ActionKind.MOVE);
         assertEquals(t.getStep(), List.of(new ActionKind[]{ActionKind.BUILD_BLOCK, ActionKind.BUILD_DOME}));

         t.nextStep(ActionKind.BUILD_BLOCK);
         assertEquals(t.getStep(), List.of(new ActionKind[]{ActionKind.END_TURN}));
         ;
    }

    @Test
    public void testStart()  {
        TurnSequence t = new DefaultTurnSequence();
        assertEquals( t.getStep(), List.of(new ActionKind[]{ActionKind.MOVE, ActionKind.MOVE_UP}) );
    }
}