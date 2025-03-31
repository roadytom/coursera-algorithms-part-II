import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
    private final WordNet wordNet;

    public Outcast(WordNet wordnet) {
        // constructor takes a WordNet object
        this.wordNet = wordnet;
    }

    public String outcast(String[] nouns) {
        // given an array of WordNet nouns, return an outcast
        int maxDistance = -1;
        String maxDistanceNoun = null;
        for (String noun : nouns) {
            int distanceSum = 0;
            for (String otherNoun : nouns) {
                distanceSum += wordNet.distance(noun, otherNoun);
            }
            if (distanceSum > maxDistance) {
                maxDistance = distanceSum;
                maxDistanceNoun = noun;
            }
        }
        return maxDistanceNoun;
    }

    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}