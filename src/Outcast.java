import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
    public Outcast(WordNet wordnet) {
        // constructor takes a WordNet object
        throw new IllegalStateException("not implemented");
    }

    public String outcast(String[] nouns) {
        // given an array of WordNet nouns, return an outcast
        throw new IllegalStateException("not implemented");
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