import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private static int n, t;
    private final double[] attempt;
    private final double x, s;
    public PercolationStats(int n, int trials) {
        int openSite;
        attempt = new double[trials];
        for (int i = 0; i < trials; i++) {
            Percolation pc = new Percolation(n);
            openSite = 0;
            while (!pc.percolates()) {
                int pos1 = StdRandom.uniform(1, n+1);
                int pos2 = StdRandom.uniform(1, n+1);
                if (!pc.isOpen(pos1, pos2)) {
                    pc.open(pos1, pos2);
                    openSite++;
                }
            }
            attempt[i] = ((double) (openSite))/(n*n);
        }
        x = mean();
        s = stddev();
    }

    public double mean() {
        return StdStats.mean(attempt);
    }

    public double stddev() {
        return StdStats.stddev(attempt);
    }

    public double confidenceLo() {

        return (x - (1.96*s)/Math.sqrt(t));
    }

    public double confidenceHi() {

        return (x + (1.96*s)/Math.sqrt(t));
    }

    public static void main(String[] args) {
        n = Integer.parseInt(args[0]);
        t = Integer.parseInt(args[1]);
        PercolationStats trial = new PercolationStats(n, t);
        StdOut.println("mean                    = " + trial.x);
        StdOut.println("stddev                  = " + trial.s);
        StdOut.println("95% confidence interval = [" + trial.confidenceLo() + ", " + trial.confidenceHi() + "]");
    }
}

