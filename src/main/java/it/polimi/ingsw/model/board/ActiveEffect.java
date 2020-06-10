package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.action.CheckEffect;

/**
 * ActiveEffect represents a persistent effect on the board for a limited number of
 * turns, every player in the game is affected by this effect.
 */
public class ActiveEffect {
    private int duration;
    private final CheckEffect effect;

    public ActiveEffect(int duration, CheckEffect effect) {
        this.duration = duration;
        this.effect = effect;
    }

    public int getDuration() {
        return duration;
    }

    public CheckEffect getEffect() {
        return effect;
    }

    /**
     * Tick down duration
     */
    public void tickTurn() {
        duration -= 1;
    }
}
