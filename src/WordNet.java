import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.Objects;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class WordNet {
    private final String synsetsFilename;
    private final String hypernymsFilename;
    private final TreeSet<String> nouns;


    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        assertNonNull(synsets);
        assertNonNull(hypernyms);
        this.synsetsFilename = synsets;
        this.hypernymsFilename = hypernyms;
        In in = new In(synsetsFilename);
        this.nouns = Stream.of(in.readAllLines())
                .map(line -> line.split(","))
                .map(line -> line[1])
                .collect(Collectors.toCollection(TreeSet::new));
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return this.nouns;
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        assertNonNull(word);
        return this.nouns.contains(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        assertTrue(isNoun(nounA));
        assertTrue(isNoun(nounB));
        throw new IllegalStateException("not implemented");
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        throw new IllegalStateException("not implemented");
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
        WordNet wordNet = new WordNet(args[0], args[1]);
        var nouns = wordNet.nouns();
        StdOut.println(StreamSupport.stream(nouns.spliterator(), false).count());
//        wordNet.nouns().forEach(StdOut::println);
        while (StdIn.hasNextLine()) {
            String noun = StdIn.readLine();
            StdOut.printf("is noun exist in sysets? -> %s\n", wordNet.isNoun(noun));
        }
    }

    private static class Synset {
        private final int synsetId;
        private final String synsetName;
        private final String gloss;

        public Synset(int synsetId, String synsetName, String gloss) {
            this.synsetId = synsetId;
            this.synsetName = synsetName;
            this.gloss = gloss;
        }

        public int getSynsetId() {
            return synsetId;
        }

        public String getSynsetName() {
            return synsetName;
        }


        public String getGloss() {
            return gloss;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Synset synset = (Synset) o;
            return synsetId == synset.synsetId;
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(synsetId);
        }
    }
}