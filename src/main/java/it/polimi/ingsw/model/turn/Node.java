package it.polimi.ingsw.model.turn;

/**
 * The Node class implements a node of a linked list of generic type,
 * each node is linked to the next and the previous node
 */
class Node<T> {
    private final T player;
    private Node<T> next;
    private Node<T> prev;

    public Node(T player) {
        this.player = player;
    }

    public T get() {
        return player;
    }

    public Node<T> getNext() {
        return next;
    }
    public void setNext(Node<T> next) {
        this.next = next;
    }

    public Node<T> getPrev() {
        return prev;
    }

    public void setPrev(Node<T> prev) {
        this.prev = prev;
    }
}

