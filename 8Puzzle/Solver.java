import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Solver {
    /* Whether the puzzle is solvable */
    private boolean solvable;
    /* Solution. null if the input board is not solvable. */
    private SearchNode finalBoard;

    /* Search node class. Stores the board state, the number of moves to
     * get to this state, and the previous Search node that led to this
     * board state. */
    private class SearchNode implements Comparable<SearchNode> {
        private final Board board;
        private final int moves;
        private final SearchNode prev;

        public SearchNode(Board b, int m, SearchNode sn) {
            board = b;
            moves = m;
            prev = sn;
        }

        /* Implements the priority function:
         * PF = distance + moves to get to the current board state
         * Uncomment the appropriate lines to use either hamming or manhattan
         * distances. */
        public int compareTo(SearchNode other) {
            // int thisHamming = this.board.hamming() + this.moves;
            // int otherHamming = this.board.hamming() + other.moves;
            // return thisHamming - otherHamming;
            int thisManhattan = this.board.manhattan() + this.moves;
            int otherManhattan = other.board.manhattan() + other.moves;
            return thisManhattan - otherManhattan;
        }
    }

    /* Solves a given input board:
     * Starting with the initial configuration and a twin configuration (with two
     * adjacent row elements swapped), simultaneously update the boards as follows:
     * If the current board (initial configuration) is a solution, store that search
     * node. The input board is solvable.
     * If the current board (twin configuration) is a solution, the input board is
     * not solvable.
     * Otherwise, look at all neighboring configurations and put them in the priority
     * queue.
     */
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

    /* Returns whether or not the input board was solvable. */
    public boolean isSolvable() {
        return solvable;
    }

    /* Returns the number of moves to solve the input board or -1 if the board wasn't
     * solvable. */
    public int moves() {
        if (this.solvable)
            return finalBoard.moves;
        return -1;
    }

    /* Returns a stack of the board configurations from the input board to the final
     * (goal) configuration. The bottom of the stack should be the input board and
     * the top of the stack should be the solved board. */
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

    /* Takes in as input a file with a board state, attempts to solve that board
     * state, then outputs the number of moves and the configurations leading to the
     * solution of the input board is solvable.
     */
    public static void main(String[] args) {
        // create initial board from file
        int n = StdIn.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = StdIn.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}