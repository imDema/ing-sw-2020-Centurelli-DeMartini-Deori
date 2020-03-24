package ingsw2020.group8.santorini.model.player;

import java.util.ArrayList;
import java.util.List;

public abstract class Player {
    private List<OnWinListener> onWinListeners = new ArrayList<OnWinListener>();
    private List<OnLossListener> onLossListeners = new ArrayList<OnLossListener>();

    private String username;
    private Pawn[] pawns;
    private God god;

    public String getUsername() {
        return username;
    }

    public God getGod() {
        return god;
    }

    public void addOnWinListener(OnWinListener listener) {
        onWinListeners.add(listener);
    }
    public void addOnLossListener(OnLossListener listener){
        onLossListeners.add(listener);
    }
}
