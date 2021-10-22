import edu.princeton.cs.algs4.Picture;

public class SeamCarver {

    private static final boolean V = true;
    private static final boolean H = false;
    private static final double BOUND_VALUE = 1000.00;
    private int[][] currentRGB;
    private int currWidth;
    private int currHeight;
    private double[][] energy;

    public SeamCarver(Picture picture) {

        if (null == picture)
            throw new NullPointerException("input picture is null");
        currHeight = picture.height();
        currWidth = picture.width();

        currentRGB = new int[currHeight][currWidth];
        for (int row = 0; row < currHeight; row++)
            for (int col = 0; col < currWidth; col++)
                currentRGB[row][col] = picture.getRGB(col, row);

        energy = new double[currHeight][currWidth];
        for (int row = 0; row < currHeight; row++)
            for (int col = 0; col < currWidth; col++)
                energy[row][col] = energy(col, row);
    }

    public Picture picture() {

        Picture seamPic = new Picture(currWidth, currHeight);
        for (int row = 0; row < currHeight; row++)
            for (int col = 0; col< currWidth; col++)
                seamPic.setRGB(col, row, currentRGB[row][col]);
        return seamPic;
    }

    public int width() {

        return currWidth;
    }

    public int height() {

        return currHeight;
    }

    public double energy(int col, int row) {

        validate(col, row);
        if (0 == col || currWidth-1 == col || 0 == row || currHeight-1 == row)
            return BOUND_VALUE;
        return Math.sqrt(xEnergySquare(col, row) + yEnergySquare(col, row));
    }

    private int xEnergySquare(int col, int row) {

        int xengery = 0;
        int[] leftRGB = getRGB(col-1, row);
        int[] rightRGB = getRGB(col+1, row);
        // 3 means three rgb components
        for (int i = 0; i < 3; i++)
            xengery = (leftRGB[i] - rightRGB[i]) * (leftRGB[i] - rightRGB[i]) + xengery;
        return xengery;
    }

    private int yEnergySquare(int col, int row) {

        int yengery = 0;
        int[] upRGB = getRGB(col, row-1);
        int[] downRGB = getRGB(col, row+1);
        for (int i = 0; i < upRGB.length; i++)
            yengery = (upRGB[i] - downRGB[i]) * (upRGB[i] - downRGB[i]) + yengery;
        return yengery;
    }

    private int[] getRGB(int col, int row) {

        int rgb = currentRGB[row][col];
        int r = (rgb >> 16) & 0xFF;
        int g = (rgb >> 8) & 0xFF;
        int b = (rgb >> 0) & 0xFF;
        int[] RGB = {
                r, g, b};
        return RGB;
    }

    private void validate(int col, int row) {

        if (col < 0 || col >= currWidth)
            throw new IllegalArgumentException("col " + col +" is not in bounds");
        if (row < 0 || row >= currHeight)
            throw new IllegalArgumentException("row " + row +" is not in bounds");
    }

    public int[] findHorizontalSeam() {
        if (currHeight == 0)
            return null;
        double[][] tenergy = new double[currWidth][currHeight];
        int transHeight = currWidth;
        int transWidth = currHeight;
        // transpose the energy matrix
        for (int row = 0; row < currHeight; row++)
            for (int col = 0; col < currWidth; col++)
                tenergy[col][row] = energy[row][col];
        return travel(transHeight, transWidth, tenergy);
    }

    public int[] findVerticalSeam() {
        if (currWidth == 0)
            return null;
        return travel(currHeight, currWidth, energy);
    }

    private int[] travel(int height, int width, double[][] energy) {
        int winner = 0;
        double winnerEnergy = Double.MAX_VALUE;

        int [][] pathTo = new int[height][width];
        double [][] totalEnergy = new double[height][width];

        // travel the energy matrix
        for (int row = 0; row < height; row++)
            for (int col = 0; col < width; col++)
                relax(row, col, width, energy, pathTo, totalEnergy);

        // get winner sequence
        for (int col = 0; col < width; col++)
            if (totalEnergy[height-1][col] < winnerEnergy) {

                winnerEnergy = totalEnergy[height-1][col];
                winner = col;
            }

        int x = winner;
        int[] trace = new int[height];
        trace[height-1] = winner;
        for (int row = height-1; row > 0; row--) {

            x = pathTo[row][x];
            trace[row-1] = x;
        }
        return trace;
    }

    private void relax(int row, int col, int width,
                       double[][] energy, int[][] pathTo, double[][] totalEnergy) {

        if (0 == row) {

            pathTo[row][col] = col;
            totalEnergy[row][col] = energy[row][col];
        } else {

            double tempEnergy;
            int parent;

            // left bound
            if (0 == col) {

                if (1 == width) {

                    tempEnergy = totalEnergy[row-1][col];
                    parent = col;
                } else if (totalEnergy[row-1][col] <= totalEnergy[row-1][col+1]) {

                    tempEnergy = totalEnergy[row-1][col];
                    parent = col;
                } else {

                    tempEnergy = totalEnergy[row-1][col+1];
                    parent = col+1;
                }
                // right bound
            } else if (width-1 == col){

                if (totalEnergy[row-1][col-1] <= totalEnergy[row-1][col]) {

                    tempEnergy = totalEnergy[row-1][col-1];
                    parent = col-1;
                } else {

                    tempEnergy = totalEnergy[row-1][col];
                    parent = col;
                }
                // general case
            } else {

                if (totalEnergy[row-1][col-1] <= totalEnergy[row-1][col]) {

                    if (totalEnergy[row-1][col-1] <= totalEnergy[row-1][col+1]) {

                        tempEnergy = totalEnergy[row-1][col-1];
                        parent = col-1;
                    } else {

                        tempEnergy = totalEnergy[row-1][col+1];
                        parent = col+1;
                    }
                } else {

                    if (totalEnergy[row-1][col] <= totalEnergy[row-1][col+1]) {

                        tempEnergy = totalEnergy[row-1][col];
                        parent = col;
                    } else {

                        tempEnergy = totalEnergy[row-1][col+1];
                        parent = col+1;
                    }
                }
            }
            totalEnergy[row][col] = energy[row][col] + tempEnergy;
            pathTo[row][col] = parent;
        }
    }

    public void removeHorizontalSeam(int[] seam) {

        checkInputSeam(seam, H);
        currHeight--;
        int transHeight = currWidth;
        int transWidth = currHeight;

        // transpose RGB matrix
        int[][] tRGB = new int[transHeight][transWidth+1];
        for (int row = 0; row < currHeight+1; row++)
            for (int col = 0; col < currWidth; col++)
                tRGB[col][row] = currentRGB[row][col];

        // transpose energy matrix
        double[][] tenergy = new double[transHeight][transWidth+1];
        for (int row = 0; row < currHeight+1; row++)
            for (int col = 0; col < currWidth; col++)
                tenergy[col][row] = energy[row][col];


        // inverse transpose RGB matrix
        int[][] swapRGB = reArrangeRGBV(seam, transHeight, transWidth, tRGB);
        int[][] invtRGB = new int[currHeight][currWidth];
        for (int row = 0; row < transHeight; row++)
            for (int col = 0; col < transWidth; col++)
                invtRGB[col][row] = swapRGB[row][col];
        currentRGB = invtRGB;

        // inverse transpose energy matrix
        double[][] swapEnergy = reComputeEnergyV(seam, transHeight, transWidth, tenergy, H);
        double[][] invtEnergy = new double[currHeight][currWidth];
        for (int row = 0; row < transHeight; row++)
            for (int col = 0; col < transWidth; col++)
                invtEnergy[col][row] = swapEnergy[row][col];
        energy = invtEnergy;
    }

    public void removeVerticalSeam(int[] seam) {

        checkInputSeam(seam, V);
        currWidth--;
        currentRGB = reArrangeRGBV(seam, currHeight, currWidth, currentRGB);
        energy = reComputeEnergyV(seam, currHeight, currWidth, energy, V);
    }

    // rearrange RGB matrix with removing a seam in vertical
    private int[][] reArrangeRGBV(int [] seam, int height, int width, int[][] currentRGB) {

        int[][] swapRGB = new int[height][width];
        int index = -1;
        for (int row = 0; row < height; row++) {

            index = seam[row];
            if (index == 0)
                System.arraycopy(currentRGB[row], 1, swapRGB[row], 0, width);
            else if (index == width)
                System.arraycopy(currentRGB[row], 0, swapRGB[row], 0, width);
            else {

                System.arraycopy(currentRGB[row], 0, swapRGB[row], 0, index);
                System.arraycopy(currentRGB[row], index+1, swapRGB[row], index, width-index);
            }
        }
        return swapRGB;
    }

    // recompute energy with removing a seam vertical
    private double[][] reComputeEnergyV(int[] seam, int height,
                                        int width, double[][] energy, boolean direction) {

        double[][] swapEnergy = new double[height][width];
        int index = -1;
        if (direction == V) {

            for (int row = 0; row < height; row++) {

                index = seam[row];
                if (index == 0) {

                    swapEnergy[row][0] = BOUND_VALUE;
                    System.arraycopy(energy[row], 2, swapEnergy[row], 1, width-1);
                } else if (index == width){

                    System.arraycopy(energy[row], 0, swapEnergy[row], 0, width-1);
                    swapEnergy[row][width-1] = BOUND_VALUE;
                }  else {

                    System.arraycopy(energy[row], 0, swapEnergy[row], 0, index-1);
                    swapEnergy[row][index-1] = energy(index-1, row);
                    swapEnergy[row][index] = energy(index, row);
                    System.arraycopy(energy[row], index+2, swapEnergy[row], index+1, width-index-1);
                }
            }
        } else {

            for (int row = 0; row < height; row++) {

                index = seam[row];
                if (index == 0) {

                    swapEnergy[row][0] = BOUND_VALUE;
                    System.arraycopy(energy[row], 2, swapEnergy[row], 1, width-1);
                } else if (index == width){

                    System.arraycopy(energy[row], 0, swapEnergy[row], 0, width-1);
                    swapEnergy[row][width-1] = BOUND_VALUE;
                } else {

                    System.arraycopy(energy[row], 0, swapEnergy[row], 0, index-1);
                    // use untransposed former RGB matrix to compute energy
                    swapEnergy[row][index-1] = energy(row, index-1);
                    swapEnergy[row][index] = energy(row, index);
                    System.arraycopy(energy[row], index+2, swapEnergy[row], index+1, width-index-1);
                }
            }
        }
        return swapEnergy;
    }

    // check input seam (if seam is null or seam is not a true seam or seam contains a out bounds num)
    private void checkInputSeam(int[] seam, boolean direction) {

        if (null == seam)
            throw new IllegalArgumentException("input seam is null");
        if (direction == V) {

            if (seam.length != currHeight || seam.length < 1)
                throw new IllegalArgumentException("input seam length is not same as current height");
            for (Integer i : seam)
                if (i < 0 || i >= currWidth)
                    throw new IllegalArgumentException("input seam contanis out bounds argument" + i);
        }
        if (direction == H) {

            if (seam.length != currWidth || seam.length < 1)
                throw new IllegalArgumentException("input seam length is not same as current width");
            for (Integer i : seam)
                if (i < 0 || i >= currHeight)
                    throw new IllegalArgumentException("input seam contanis out bounds argument" + i);
        }

        int pre;
        pre = seam[0];
        for (Integer i : seam) {

            if (Math.abs(i - pre) > 1)
                throw new IllegalArgumentException("input seam is not a true seam" + i + pre);
            pre = i;
        }
    }

    //  unit testing (optional)
    public static void main(String[] args) {

        Picture pic = new Picture("/home/mayuri/Desktop/seam/7x3.png");
        SeamCarver sc = new SeamCarver(pic);
        int[] seam = {
                5,6,4};
        sc.removeVerticalSeam(seam);
    }
}