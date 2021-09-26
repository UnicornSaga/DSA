import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public class FastCollinearPoints {
    private final int countSegment;
    private final ArrayList<LineSegment> ls = new ArrayList<LineSegment>();

    public FastCollinearPoints(Point[] points) {
        int countSegment1;
        if (points == null) throw new IllegalArgumentException();
        countSegment1 = 0;
        int n = points.length;
        Point[] pointByValue = points.clone();
        ArrayList<Point> usedPoint = new ArrayList<Point>();
        for (int i = 0; i < n; i++) {
            Arrays.sort(pointByValue);
            Arrays.sort(pointByValue, pointByValue[i].slopeOrder());
            for (int curr = 0, first = 1, last = 2; last < n; last++) {
                while (last < n && (pointByValue[curr].slopeTo(pointByValue[first]) == pointByValue[curr].slopeTo(pointByValue[last]))) {
                    last++;
                }
                if (last - first >= 3 && pointByValue[curr].compareTo(pointByValue[first]) < 0) {
                    ls.add(new LineSegment(pointByValue[curr], pointByValue[last-1]));
                    countSegment1++;
                }
                first = last;
            }
        }
        countSegment = countSegment1;
    }

    public int numberOfSegments() {
        return countSegment;
    }

    public LineSegment[] segments() {
        LineSegment[] tmp = ls.toArray(new LineSegment[countSegment]);
        return tmp;
    }

    public static void main(String[] args) {

        // read the n points from a file
        int n = StdIn.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = StdIn.readInt();
            int y = StdIn.readInt();
            points[i] = new Point(x, y);
        }

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
        }
    }
}
