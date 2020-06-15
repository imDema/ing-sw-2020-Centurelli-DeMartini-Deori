package it.polimi.ingsw.model.turn;

/**
 * Circular doubly-linked list of elements
 */
public class CircularList<T> {
    private Node<T> current = null;
    private int size = 0;

    public T current() {
        return current.get();
    }

    /**
     * Add an item before the current item (last before looping)
     * @param content item to insert
     */
    public void add(T content) {
        if (current == null) {
            current = new Node<>(content);
            current.setNext(current);
            current.setPrev(current);
        } else {
            Node<T> last = current.getPrev();
            Node<T> newNode = new Node<>(content);
            last.setNext(newNode);
            newNode.setPrev(last);
            newNode.setNext(current);
            current.setPrev(newNode);
        }
        size += 1;
    }

    /**
     * Remove an item from the list if present. If two or more copies of the item are in the list only the first
     * one will be removed.
     * @param content item to remove
     * @return true if the item was removed, false if it was not in the list
     */
    public boolean remove(T content) {
        if (current.get().equals(content)) {
            current = current.getPrev();
        }
        return removeRecursive(current, content, 0);
    }

    /**
     * @return number of elements in the list
     */
    public int getSize() {
        return size;
    }

    private boolean removeRecursive(Node<T> node, T content, int iteration) {
        if (node.get().equals(content)) {
            Node<T> prev = node.getPrev();
            Node<T> next = node.getNext();

            prev.setNext(next);
            next.setPrev(prev);
            size -= 1;
            return true;
        } else if (iteration <= size) {
            return removeRecursive(node.getNext(), content, iteration + 1);
        } else {
            return false;
        }
    }

    public void next() {
        current = current.getNext();
    }
}
