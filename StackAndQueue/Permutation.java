import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Permutation {
    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);
        RandomizedQueue<String> rq = new RandomizedQueue<String>();
        while (!StdIn.isEmpty()) {
            String tmp = StdIn.readString();
            rq.enqueue(tmp);
        }
        for (int i = 1; i <= k; i++) {
            String res = rq.dequeue();
            StdOut.println(res);
        }
    }
}
