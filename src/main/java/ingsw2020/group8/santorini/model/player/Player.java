package ingsw2020.group8.santorini.model.player;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private List<OnWinListener> onWinListeners = new ArrayList<>();
    private List<OnLossListener> onLossListeners = new ArrayList<>();

    private String username;
    private Pawn[] pawns = new Pawn[2];
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

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Player && username.equals(((Player) obj).username);
    }

    public Player(String username) {
        this.username = username;
    }
}
