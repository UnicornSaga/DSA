public class Outcast {
    private final WordNet wordnet;
    public Outcast(WordNet wordnet) {
        this.wordnet = wordnet;
    }

    public String outcast(String[] nouns) {
        int maxDistance = 0;
        int id = 0;
        for (int i = 0; i < nouns.length; i++) {
            int distance = 0;
            for (int k = 0; k < nouns.length; k++) {
                distance += wordnet.distance(nouns[i], nouns[k]);
            }
            if (distance > maxDistance) {
                maxDistance = distance;
                id = i;
            }
        }
        return nouns[id];
    }
}
