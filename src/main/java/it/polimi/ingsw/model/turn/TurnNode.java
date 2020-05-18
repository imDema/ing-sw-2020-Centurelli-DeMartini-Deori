package it.polimi.ingsw.model.turn;

import it.polimi.ingsw.model.player.Player;

class TurnNode {
    private final Player player;
    private TurnNode nextNode;

    public TurnNode(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public TurnNode getNextNode() {
        return nextNode;
    }

    public void setNextNode(TurnNode nextNode) {
        this.nextNode = nextNode;
    }
}

