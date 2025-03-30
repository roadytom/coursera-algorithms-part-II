import java.util.TreeMap;
import java.util.TreeSet;

public class WordNet {
    private final String synsetsFilename;
    private final String hypernymsFilename;
    private final TreeSet<>


    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        assertNonNull(synsets);
        assertNonNull(hypernyms);
        this.synsetsFilename = synsets;
        this.hypernymsFilename = hypernyms;
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {

    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        assertNonNull(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        assertTrue(isNoun(nounA));
        assertTrue(isNoun(nounB));

    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {

    }

    private void assertNonNull(Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException("given obj is null");
        }
    }

    private void assertTrue(boolean value) {
        if (!value) {
            throw new IllegalArgumentException("value is false, when expected true");
        }
    }

    // do unit testing of this class
    public static void main(String[] args) {

    }
}