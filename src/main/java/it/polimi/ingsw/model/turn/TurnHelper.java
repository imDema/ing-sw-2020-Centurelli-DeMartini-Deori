package it.polimi.ingsw.model.turn;

import it.polimi.ingsw.model.player.Player;

public class TurnHelper {
    private TurnNode current = null;
    private int size = 0;

    public Player current() {
        return current.get();
    }

    public void add(Player player) {
        if (current == null) {
            current = new TurnNode(player);
            current.setNext(current);
            current.setPrev(current);
        } else {
            TurnNode last = current.getPrev();
            TurnNode newNode = new TurnNode(player);
            last.setNext(newNode);
            newNode.setPrev(last);
            newNode.setNext(current);
            current.setPrev(newNode);
        }
        size += 1;
    }

    public void remove(Player player) {
        if (current.get().equals(player)) {
            current = current.getPrev();
            size -= 1;
        }
        removeRecursive(current, player, 0);
    }

    private void removeRecursive(TurnNode node, Player player, int iteration) {
        if (node.get().equals(player)) {
            TurnNode prev = node.getPrev();
            TurnNode next = node.getNext();

            prev.setNext(next);
            next.setPrev(prev);
            size -= 1;
        } else if (iteration <= size) {
            removeRecursive(node.getNext(), player, iteration + 1);
        }
    }

    public void next() {
        current = current.getNext();
    }
}
