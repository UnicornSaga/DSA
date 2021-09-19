import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdRandom;

public class RandomWord {
    public static void main(String[] args){
        double i = 2;
        String res = StdIn.readString();
        while (!StdIn.isEmpty()){
            String tmp = StdIn.readString();
            if (StdRandom.bernoulli(1/i)) res = tmp;
            i++;
        }
        System.out.println(res);
    }
}
