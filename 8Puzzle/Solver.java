import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Solver {
    private boolean solvable;
    private SearchNode finalBoard;

    private class SearchNode implements Comparable<SearchNode> {
        private final Board board;
        private final int moves;
        private final SearchNode prev;

        public SearchNode(Board b, int m, SearchNode sn) {
            board = b;
            moves = m;
            prev = sn;
        }

        public int compareTo(SearchNode other) {
            int thisManhattan = this.board.manhattan() + this.moves;
            int otherManhattan = other.board.manhattan() + other.moves;
            return thisManhattan - otherManhattan;
        }
    }

    public Solver(Board initial) {
        MinPQ<SearchNode> initPQ = new MinPQ<SearchNode>();
        MinPQ<SearchNode> twinPQ = new MinPQ<SearchNode>();
        initPQ.insert(new SearchNode(initial, 0, null));
        twinPQ.insert(new SearchNode(initial.twin(), 0, null));
        SearchNode initSN;
        SearchNode twinSN;
        while (true) {
            initSN = initPQ.delMin();
            twinSN = twinPQ.delMin();

            if (initSN.board.isGoal()) {
                finalBoard = initSN;
                solvable = true;
                break;
            }
            if (twinSN.board.isGoal()) {
                finalBoard = twinSN;
                solvable = false;
                break;
            }
            for (Board initBoard : initSN.board.neighbors()) {
                if (initSN.prev == null || !initBoard.equals(initSN.prev.board))
                    initPQ.insert(new SearchNode(initBoard, initSN.moves + 1,
                            initSN));
            }
            for (Board twinBoard : twinSN.board.neighbors()) {
                if (twinSN.prev == null || !twinBoard.equals(twinSN.prev.board))
                    twinPQ.insert(new SearchNode(twinBoard, twinSN.moves + 1,
                            twinSN));
            }
        }
    }

    public boolean isSolvable() {
        return solvable;
    }

    public int moves() {
        if (this.solvable)
            return finalBoard.moves;
        return -1;
    }

    public Iterable<Board> solution() {
        if (this.solvable) {
            // Stack is from algs4 library
            Stack<Board> s = new Stack<Board>();
            SearchNode curr = finalBoard;
            while (curr != null) {
                s.push(curr.board);
                curr = curr.prev;
            }
            return s;
        }
        return null;
    }

    public static void main(String[] args) {
        // create initial board from file
        int n = StdIn.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = StdIn.readInt();
        Board initial = new Board(blocks);

        Solver solver = new Solver(initial);

        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}