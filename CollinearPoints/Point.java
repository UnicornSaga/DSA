import edu.princeton.cs.algs4.StdDraw;
import java.util.Comparator;

public class Point implements Comparable<Point> {
    private final int x;
    private final int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void draw() {
        StdDraw.point(x, y);
    }

    public void drawTo(Point that) {
        StdDraw.line(this.x, this.y, that.x, that.y);
    }

    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    public int compareTo(Point that) {
        if ((this.y < that.y) || (this.y == that.y && this.x < that.x)) return -1;
        else if ((this.y > that.y) || (this.y == that.y && this.x > that.x)) return 1;
        return 0;
    }

    public double slopeTo(Point that) {
        double slope = 0.0;
        if (this.y == that.y && this.x != that.x) return slope;
        else if (this.x == that.x && this.y != that.y) {
            slope = Double.POSITIVE_INFINITY;
            return slope;
        }
        else if (compareTo(that) == 0) {
            slope = Double.NEGATIVE_INFINITY;
            return slope;
        }
        slope = (double) (that.y - this.y) / (that.x - this.x);
        return slope;
    }

    public Comparator<Point> slopeOrder() {
        return new SlopeCompare();
    }

    private class SlopeCompare implements Comparator<Point> {
        public int compare(Point a, Point b) {
            double slopeA = a.slopeTo(Point.this);
            double slopeB = b.slopeTo(Point.this);
            if (Double.compare(slopeA, slopeB) == 1) return 1;
            else if (Double.compare(slopeA, slopeB) == -1) return -1;
            return 0;
        }
    }
}
