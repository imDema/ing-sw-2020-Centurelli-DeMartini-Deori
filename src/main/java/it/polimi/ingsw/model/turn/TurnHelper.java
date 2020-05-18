package it.polimi.ingsw.model.turn;

import it.polimi.ingsw.model.player.Player;

public class TurnHelper {
    private TurnNode head;
    private TurnNode tail;
    private TurnNode current;

    public TurnHelper() {
        head = null;
        tail = null;
    }

    public Player current() {
        return current.getPlayer();
    }

    public void add(Player player) {
        TurnNode newNode = new TurnNode(player);
        if (head == null) {
            head = newNode;
            current = head;
        } else {
            tail.setNextNode(newNode);
        }
        tail = newNode;
        tail.setNextNode(head);
    }

    public void remove(Player player) {
        TurnNode currentNode = head;
        if (head != null) {
            if (currentNode.getPlayer().equals(player)) {
               head = head.getNextNode();
               tail.setNextNode(head);
            } else {
                do {
                    TurnNode nextNode = currentNode.getNextNode();
                    if (nextNode.getPlayer().equals(player)) {
                        if(current.getPlayer().equals(player))
                            current = current.getNextNode();
                        currentNode.setNextNode(nextNode.getNextNode());
                        break;
                    }
                    currentNode.setNextNode(currentNode);
                } while (currentNode != head);
            }
        }
    }

    public void next() {
        current = current.getNextNode();
    }
}
