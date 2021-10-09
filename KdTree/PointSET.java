import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.Stack;
import java.util.Iterator;

public class PointSET {
    private final SET<Point2D> tree;

    public PointSET() {
        tree = new SET<>();
    }

    public boolean isEmpty() {
        return tree.isEmpty();
    }

    public int size() {
        return tree.size();
    }

    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        tree.add(p);
    }

    public boolean contains(Point2D p) {
        return tree.contains(p);
    }

    public void draw() {
        for (Point2D tmp : tree) {
            tmp.draw();
        }
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException();
        }
        Stack<Point2D> st = new Stack<>();
        Iterator<Point2D> it = tree.iterator();
        while (it.hasNext()) {
            Point2D tmp = it.next();
            if (rect.contains(tmp)) {
                st.push(tmp);
            }
        }
        return st;
    }

    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        Point2D res = null;
        double smallestDistance = Double.POSITIVE_INFINITY;
        Iterator<Point2D> it = tree.iterator();
        while (it.hasNext()) {
            Point2D tmp = it.next();
            double distance = p.distanceSquaredTo(tmp);
            if (distance < smallestDistance) {
                smallestDistance = distance;
                res = tmp;
            }
        }
        return res;
    }
}
