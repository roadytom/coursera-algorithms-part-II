import edu.princeton.cs.algs4.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static edu.princeton.cs.algs4.StdOut.print;
import static edu.princeton.cs.algs4.StdOut.println;

public class WordNet {
    private TreeMap<String, List<Integer>> nounToIds;
    private SAP sap;


    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        assertNonNull(synsets);
        assertNonNull(hypernyms);
        initNouns(synsets);
        initSap(hypernyms);
    }

    private void initSap(String hypernymsFilename) {
        In in = new In(hypernymsFilename);
        int V = nounToIds.values().stream().map(List::size).mapToInt(Integer::intValue).sum();
        Digraph digraph = new Digraph(V);
        Stream.of(in.readAllLines()).map(line -> line.split(",")).forEach(line -> {
            int synsetId = Integer.parseInt(line[0]);
            print(synsetId + " ->");
            for (int i = 1; i < line.length; i++) {
                int hypernymId = Integer.parseInt(line[i]);
                print(" " + hypernymId);
                digraph.addEdge(synsetId, hypernymId);
            }
            println();
        });
        sap = new SAP(digraph);
    }

    private void initNouns(String synsetsFilename) {
        In in = new In(synsetsFilename);
        this.nounToIds = Stream.of(in.readAllLines()).map(line -> line.split(",")).map(Synset::new).collect(Collectors.groupingBy(Synset::getSynsetName, TreeMap::new, Collectors.mapping(Synset::getSynsetId, Collectors.toList())));
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return this.nounToIds.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        assertNonNull(word);
        return this.nounToIds.containsKey(word);
    }

    public String getNounById(int synsetId) {
        return nounToIds.entrySet().stream()
                .filter(entry -> entry.getValue().contains(synsetId))
                .map(Map.Entry::getKey)
                .findAny()
                .orElse(null);
    }


    public Map<Integer, Iterable<String>> getChildren(String noun) {
        assertTrue(isNoun(noun));
        Map<Integer, Iterable<String>> nodeWithChildren = new HashMap<>();
        var noundIds = nounToIds.get(noun);
        for (Integer noundId : noundIds) {
            var children = sap.getGraph().adj(noundId);
            nodeWithChildren.put(noundId, StreamSupport.stream(children.spliterator(), false).map(id -> id + "-" + getNounById(id)).collect(Collectors.toUnmodifiableList()));
        }
        return nodeWithChildren;
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        assertTrue(isNoun(nounA));
        assertTrue(isNoun(nounB));
        List<Integer> synsetsA = nounToIds.get(nounA);
        List<Integer> synsetsB = nounToIds.get(nounB);
        return sap.length(synsetsA, synsetsB);
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        assertTrue(isNoun(nounA));
        assertTrue(isNoun(nounB));
        List<Integer> synsetsA = nounToIds.get(nounA);
        List<Integer> synsetsB = nounToIds.get(nounB);
        int synsetId = sap.ancestor(synsetsA, synsetsB);
        return getNounById(synsetId);
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
        StdOut.println(wordNet.nounToIds.values().stream().map(List::size).mapToInt(Integer::intValue).sum());
        StdOut.printf("Enter method name: \n");
        while (StdIn.hasNextLine()) {
            String option = StdIn.readLine();
            if ("sap".equals(option)) {
                String vNoun = StdIn.readLine();
                String wNoun = StdIn.readLine();
                String sapNoun = wordNet.sap(vNoun, wNoun);
                StdOut.printf("Shortest Ancestral Noun = %s\n", sapNoun);
            } else if ("distance".equals(option)) {
                String vNoun = StdIn.readLine();
                String wNoun = StdIn.readLine();
                int sapLength = wordNet.distance(vNoun, wNoun);
                StdOut.printf("Shortest Ancestral Length = %s\n", sapLength);
            } else if ("isNoun".equals(option)) {
                String noun = StdIn.readLine();
                StdOut.printf("isNoun: %s\n", wordNet.isNoun(noun));
            } else if ("nouns".equals(option)) {
                StdOut.printf("Nouns length: %d\n", StreamSupport.stream(wordNet.nouns().spliterator(), false).count());
            } else if ("children".equals(option)) {
                String noun = StdIn.readLine();
                wordNet.nounToIds.getOrDefault(noun, List.of())
                        .forEach(idx -> {
                            StdOut.printf("%d-%s -> %s\n", idx, noun, wordNet.getChildren(noun));
                        });
            } else {
                throw new IllegalArgumentException("non-existing method");
            }
            StdOut.printf("Enter method name: \n");
        }
    }

    /**
     * happening occurrence occurrent natural_event
     * resistance opposition
     */

    private static class Synset {
        private final int synsetId;
        private final String synsetName;
        private final String gloss;

        public Synset(int synsetId, String synsetName, String gloss) {
            this.synsetId = synsetId;
            this.synsetName = synsetName;
            this.gloss = gloss;
        }

        public Synset(String[] line) {
            this.synsetId = Integer.parseInt(line[0]);
            this.synsetName = line[1];
            this.gloss = line[2];
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