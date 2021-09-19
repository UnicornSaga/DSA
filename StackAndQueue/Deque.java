import edu.princeton.cs.algs4.StdOut;
import java.util.Iterator;

public class Deque<Item> implements Iterable<Item> {
    private int n;
    private Node first, last;

    private class Node {
        Item item;
        Node next;
        Node prev;
    }

    public Deque() {
        n = 0;
        first = null;
        last = null;
    }

    public boolean isEmpty() {
        return n == 0;
    }

    public int size() {
        return n;
    }

    public void addFirst(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        else {
            Node tmp = first;
            first = new Node();
            first.item = item;
            first.next = tmp;
            first.prev = null;
            // TH khi chua co node nao thi khi nhet them first va last cung tro vao 1 node
            if (tmp == null && last == null) last = first;
            else tmp.prev = first;
            n++;
        }
    }

    public void addLast(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        else {
            Node tmp = last;
            last = new Node();
            last.item = item;
            last.next = null;
            // TH khi chua co node nao thi khi nhet them first va last cung tro vao 1 node
            if (tmp == null && first == null) first = last;
            else {
                tmp.next = last;
                last.prev = tmp;
            }
            n++;
        }
    }

    public Item removeFirst() {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException();
        }
        else {
            Item rmvFirst = first.item;
            first = first.next;
            // TH neu node can xoa la node cuoi cung
            if (first == null) {
                last = null;
            }
            else {
                first.prev = null;
            }
            n--;
            return rmvFirst;
        }
    }

    public Item removeLast() {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException();
        }
        else {
            Item rmvLast = last.item;
            last = last.prev;
            // TH neu node can xoa la node cuoi cung
            if (last == null) {
                first = null;
            }
            else {
                last.next = null;
            }
            n--;
            return rmvLast;
        }
    }

    public Iterator<Item> iterator() {
        return new DequeueIterator();
    }

    private class DequeueIterator implements Iterator<Item> {
        private Node curr = first;

        public boolean hasNext() {
            return curr != null;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (curr.item == null) {
                throw new java.util.NoSuchElementException();
            }
            else {
                Item item = curr.item;
                curr = curr.next;
                return item;
            }
        }
    }

    public static void main(String[] args) {
        Deque<Integer> deque = new Deque<>();
        StdOut.println("DequeIsEmpty: " + deque.isEmpty());
        StdOut.println("Deque's Size: " + deque.size());

        deque.addFirst(1);
        deque.addFirst(2);
        deque.removeFirst();
        deque.addLast(1);
        deque.addLast(2);
        deque.removeFirst();
        deque.addFirst(1);
        deque.removeFirst();
        deque.addLast(1);

        Iterator<Integer> it = deque.iterator();
        while (it.hasNext()) {
            Integer i = it.next();
            StdOut.println("it: " + i);
        }

        StdOut.println("Size: " + deque.size());
        StdOut.println("Remove First: " + deque.removeFirst());
        StdOut.println("Remove Last: " + deque.removeLast());
        StdOut.println("Size: " + deque.size());
    }
}
