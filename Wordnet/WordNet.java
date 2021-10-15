import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;

import java.util.ArrayList;
import java.util.HashMap;

public class WordNet {
    private final ArrayList<String> idToNound;
    private final HashMap<String, ArrayList<Integer>> nounToId;
    private final SAP sap;

    public WordNet(String synsets, String hypernyms) {
        Digraph G;
        In in = new In(synsets);
        idToNound = new ArrayList<>();
        nounToId = new HashMap<>();
        while (in.hasNextLine()) {
            String line = in.readLine();
            String[] storeLine = line.split(",");
            int id = Integer.parseInt(storeLine[0]);
            if (idToNound.size() < id) {
                idToNound.ensureCapacity(id + 1);
            }
            idToNound.add(storeLine[1]);
            String[] tmp = storeLine[1].split(" ");
            for (String noun : tmp) {
                if (!nounToId.containsKey(noun)) {
                    nounToId.put(noun, new ArrayList<Integer>());
                }
                nounToId.get(noun).add(id);
            }
        }

        G = new Digraph(idToNound.size());
        in = new In(hypernyms);
        while (in.hasNextLine()) {
            String line = in.readLine();
            String[] storeLine = line.split(",");
            int v = Integer.parseInt(storeLine[0]);
            for (int i = 1; i < storeLine.length; i++) {
                int w = Integer.parseInt(storeLine[i]);
                G.addEdge(v, w);
            }
        }
        sap = new SAP(G);
    }

    public Iterable<String> nouns() {
        Stack<String> st = new Stack<>();
        for (String s : nounToId.keySet()) {
            st.push(s);
        }
        return st;
    }

    public boolean isNoun(String word) {
        return nounToId.containsKey(word);
    }

    public int distance(String nounA, String nounB) {
        ArrayList<Integer> id1 = nounToId.get(nounA);
        ArrayList<Integer> id2 = nounToId.get(nounB);
        return sap.length(id1, id2);
    }

    public String sap(String nounA, String nounB) {
        ArrayList<Integer> id1 = nounToId.get(nounA);
        ArrayList<Integer> id2 = nounToId.get(nounB);
        int idAncs = sap.ancestor(id1, id2);
        return idToNound.get(idAncs);
    }
}
