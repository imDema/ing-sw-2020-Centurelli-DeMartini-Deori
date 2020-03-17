package ingsw2020.group8.santorini.player;

import ingsw2020.group8.santorini.Action;
import ingsw2020.group8.santorini.Board;
import ingsw2020.group8.santorini.Pawn;

import java.util.ArrayList;
import java.util.List;

public abstract class Player {
    private List<OnWinListener> onWinListeners = new ArrayList<OnWinListener>();
    private List<OnLossListener> onLossListeners = new ArrayList<OnLossListener>();

    private List<Action>  lastActions;
    private Pawn[] pawns;
    private boolean isTurnFinished;

    public abstract List<Action> getActions(Board b);
    public abstract boolean isTurnFinished();

    public abstract void startTurn();
    public abstract void endTurn();

    public void addOnWinListener(OnWinListener listener) {
        onWinListeners.add(listener);
    }
    public void addOnLossListener(OnLossListener listener){
        onLossListeners.add(listener);
    }
}
