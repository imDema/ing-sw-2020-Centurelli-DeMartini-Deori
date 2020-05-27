package it.polimi.ingsw.model.turn;

import it.polimi.ingsw.model.player.Player;

class TurnNode {
    private final Player player;
    private TurnNode next;
    private TurnNode prev;

    public TurnNode(Player player) {
        this.player = player;
    }

    public Player get() {
        return player;
    }

    public TurnNode getNext() {
        return next;
    }
    public void setNext(TurnNode next) {
        this.next = next;
    }

    public TurnNode getPrev() {
        return prev;
    }

    public void setPrev(TurnNode prev) {
        this.prev = prev;
    }
}

