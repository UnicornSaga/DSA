import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import java.util.Iterator;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private int n;
    private Node first, last;

    private class Node {
        Item item;
        Node next;
        Node prev;
    }

    public RandomizedQueue() {
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

    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        else {
            Node tmp = last;
            last = new Node();
            last.item = item;
            last.next = null;
            // TH khi chua co phan tu nao ma nhet node thi first va last cung tro vao 1 node
            if (tmp == null && first == null) first = last;
            else {
                last.prev = tmp;
                tmp.next = last;
            }
            n++;
        }
    }

    public Item dequeue() {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException();
        }
        Item item;
        Node tmp = first;
        int randPos = StdRandom.uniform(n);
        int pos = 0;
        while (pos < randPos) {
            tmp = tmp.next;
            pos++;
        }
        item = tmp.item;

        // TH1: node nam o vi tri dau va sau do la null
        if (tmp == first && tmp.next == null) {
            first = null;
            last = null;
        }
        // TH2: node nam o vi tri dau va dang sau van con cac node khac
        else if (tmp == first) {
            first = first.next;
            first.prev = null;
        }
        // TH3: node dung cuoi cung
        else if (tmp == last) {
            last = tmp.prev;
            last.next = null;
        }
        else {
            tmp.prev.next = tmp.next;
            tmp.next.prev = tmp.prev;
        }
        n--;
        return item;
    }

    public Item sample() {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException();
        }
        int randPos = StdRandom.uniform(n);
        int pos = 0;
        Item item;
        Node tmp = first;
        while (pos < randPos) {
            tmp = tmp.next;
            pos++;
        }
        item = tmp.item;
        return item;
    }

    public Iterator<Item> iterator() {
        return new RandomQueueIterator();
    }

    private class RandomQueueIterator implements Iterator<Item> {
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
            Item item = curr.item;
            curr = curr.next;
            return item;
        }
    }

    public static void main(String[] args) {
        RandomizedQueue<Integer> rq = new RandomizedQueue<>();
        StdOut.println("RandomizedQueue is Empty: " + rq.isEmpty());
        StdOut.println("size: " + rq.size());

        rq.enqueue(283);
        StdOut.println(rq.sample());

        rq.enqueue(119);

        StdOut.println(rq.dequeue());
        StdOut.println(rq.dequeue());

        for (Integer i = 0; i < 5; i++) {
            rq.enqueue(i);
        }

        StdOut.println("size after enqueue: " + rq.size());
        StdOut.println("Deque: " + rq.dequeue());
        StdOut.println("Sample: " + rq.sample());

        Iterator<Integer> jt = rq.iterator();
        while (jt.hasNext()) {
            Integer i = jt.next();
            StdOut.println("it: " + i);
        }
    }
}
