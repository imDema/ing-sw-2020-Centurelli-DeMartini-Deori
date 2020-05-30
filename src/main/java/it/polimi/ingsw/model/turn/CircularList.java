package it.polimi.ingsw.model.turn;

public class CircularList<T> {
    private Node<T> current = null;
    private int size = 0;

    public T current() {
        return current.get();
    }

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

    public void remove(T content) {
        if (current.get().equals(content)) {
            current = current.getPrev();
        }
        removeRecursive(current, content, 0);
    }

    public int getSize() {
        return size;
    }

    private void removeRecursive(Node<T> node, T content, int iteration) {
        if (node.get().equals(content)) {
            Node<T> prev = node.getPrev();
            Node<T> next = node.getNext();

            prev.setNext(next);
            next.setPrev(prev);
            size -= 1;
        } else if (iteration <= size) {
            removeRecursive(node.getNext(), content, iteration + 1);
        }
    }

    public void next() {
        current = current.getNext();
    }
}
