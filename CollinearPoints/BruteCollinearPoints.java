import java.util.Arrays;

public class BruteCollinearPoints {
    private final int countSegment;
    private final LineSegment[] ls;

    public BruteCollinearPoints(Point[] points) {
        int countSegment1;
        if (points == null) throw new IllegalArgumentException();
        int n = points.length;
        countSegment1 = 0;
        ls = new LineSegment[n];
        for (int i = 0; i < n; i++) {
            if (points[i] == null) throw new IllegalArgumentException();
            for (int k = i + 1; k < n; k++) {
                if (points[k] == null) throw new IllegalArgumentException();
                if (points[k].compareTo(points[i]) == 0) throw new IllegalArgumentException();
                double seg1 = points[i].slopeTo(points[k]);
                for (int j = k + 1; j < n; j++) {
                    if (points[j] == null) throw new IllegalArgumentException();
                    if (points[j].compareTo(points[k]) == 0 || points[j].compareTo(points[i]) == 0) throw new IllegalArgumentException();
                    double seg2 = points[j].slopeTo(points[k]);
                    for (int m = j + 1; m < n; m++) {
                        if (points[m] == null) throw new IllegalArgumentException();
                        if (points[m].compareTo(points[j]) == 0 || points[m].compareTo(points[k]) == 0 || points[m].compareTo(points[i]) == 0) throw new IllegalArgumentException();
                        double seg3 = points[m].slopeTo(points[j]);
                        if (seg1 == seg2 && seg2 == seg3) {
                            // add cas diem vao mang moi de sort, sau do lay ra 2 diem o dau va cuoi
                            Point[] tmp = new Point[4];
                            tmp[0] = points[i];
                            tmp[1] = points[k];
                            tmp[2] = points[j];
                            tmp[3] = points[m];
                            Arrays.sort(tmp);
                            ls[countSegment1] = new LineSegment(tmp[0], tmp[3]);
                            countSegment1++;
                        }
                    }
                }
            }
        }
        countSegment = countSegment1;
    }

    public int numberOfSegments() {
        return countSegment;
    }

    public LineSegment[] segments() {
        LineSegment[] tmp = new LineSegment[countSegment];
        for (int i = 0; i < countSegment; i++) {
            tmp[i] = ls[i];
        }
        return tmp;
    }
}
