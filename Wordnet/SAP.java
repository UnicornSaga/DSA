import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;

public class SAP {
    private final Digraph G;

    public SAP(Digraph G) {
        this.G = new Digraph(G);
    }

    public int length(int v, int w) {
        int minLength = Integer.MAX_VALUE;
        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(G, w);
        for (int i = 0; i < G.V(); i++) {
            if (bfsV.hasPathTo(i) && bfsW.hasPathTo(i)) {
                int distance = bfsV.distTo(i) + bfsW.distTo(i);
                if (distance < minLength) {
                    minLength = distance;
                }
            }
        }
        return (minLength == Integer.MAX_VALUE) ? -1 : minLength;
    }

    public int ancestor(int v, int w) {
        int minLength = Integer.MAX_VALUE;
        int ancs = 0;
        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(G, w);
        for (int i = 0; i < G.V(); i++) {
            if (bfsV.hasPathTo(i) && bfsW.hasPathTo(i)) {
                int distance = bfsV.distTo(i) + bfsW.distTo(i);
                if (distance < minLength) {
                    minLength = distance;
                    ancs = i;
                }
            }
        }
        return (minLength == Integer.MAX_VALUE) ? -1 : ancs;
    }

    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        int minLength = Integer.MAX_VALUE;
        for (int ver1 : v) {
            for (int ver2 : w) {
                int distance = length(ver1, ver2);
                if (distance != -1 && distance < minLength) {
                    minLength = distance;
                }
            }
        }
        return (minLength == Integer.MAX_VALUE) ? -1 : minLength;
    }

    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        int minLength = Integer.MAX_VALUE;
        int ancs = 0;
        for (int ver1 : v) {
            for (int ver2 : w) {
                int distance = length(ver1, ver2);
                int tmpAncs = ancestor(ver1, ver2);
                if (distance != -1 && distance < minLength) {
                    minLength = distance;
                    ancs = tmpAncs;
                }
            }
        }
        return (minLength == Integer.MAX_VALUE) ? -1 : ancs;
    }
}
