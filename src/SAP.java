import edu.princeton.cs.algs4.*;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class SAP {
    private final Digraph graph;

    public Digraph getGraph() {
        return graph;
    }

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        this.graph = G;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        return ancestor(List.of(v), List.of(w));
    }


    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        return findSapResult(v, w).getSapAncestor();
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        return length(List.of(v), List.of(w));
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        return findSapResult(v, w).getLengthOrMinusOne();
    }

    private SAPResult findSapResult(Iterable<Integer> v, Iterable<Integer> w) {
        List<Integer> vDistances = Stream.generate(() -> -1).limit(graph.V()).collect(Collectors.toList());
        List<Integer> wDistances = Stream.generate(() -> -1).limit(graph.V()).collect(Collectors.toList());
        Queue<Integer> vQueue = new ArrayDeque<>();
        Queue<Integer> wQueue = new ArrayDeque<>();
        v.forEach(x -> {
            if (x >= graph.V() || x < 0) {
                throw new IllegalArgumentException("x is out of range");
            }
            vDistances.set(x, 0);
            vQueue.offer(x);
        });
        w.forEach(x -> {
            if (x >= graph.V() || x < 0) {
                throw new IllegalArgumentException("x is out of range");
            }
            wDistances.set(x, 0);
            wQueue.offer(x);
        });
        SAPResult sapResult = new SAPResult(-1, Integer.MAX_VALUE);
        var distances = List.of(vDistances, wDistances);
        var queues = List.of(vQueue, wQueue);
        while (!vQueue.isEmpty() || !wQueue.isEmpty()) {
            IntStream.range(0, 2).forEach(i -> {
                Queue<Integer> queue = queues.get(i);
                List<Integer> distance = distances.get(i);
                if (!queue.isEmpty()) {
                    int poppedElement = queue.poll();
                    if (isShorter(vDistances, wDistances, poppedElement, sapResult.getSapLength())) {
                        sapResult.setSapLength(wDistances.get(poppedElement) + vDistances.get(poppedElement));
                        sapResult.setSapAncestor(poppedElement);
                    }
                    updateNeighborDistanceAndAddQueue(queue, distance, poppedElement);
                }
            });
            Stream.of(vQueue, wQueue).forEach(queue -> {

            });
        }
        return sapResult;
    }

    private void updateNeighborDistanceAndAddQueue(Queue<Integer> queue, List<Integer> distances, int currentElement) {
        for (int neighbor : graph.adj(currentElement)) {
            queue.offer(neighbor);
            distances.set(neighbor, distances.get(currentElement) + 1);
        }
    }

    private boolean isShorter(List<Integer> vDistances, List<Integer> wDistances, int currentElement, int shortAncestorDistance) {
        return vDistances.get(currentElement) != -1 && wDistances.get(currentElement) != -1 && wDistances.get(currentElement) + vDistances.get(currentElement) <= shortAncestorDistance;
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }

    private static class SAPResult {
        private int sapAncestor;
        private int sapLength;

        public SAPResult(int sapAncestor, int sapLength) {
            this.sapAncestor = sapAncestor;
            this.sapLength = sapLength;
        }

        public void setSapAncestor(int sapAncestor) {
            this.sapAncestor = sapAncestor;
        }

        public int getLengthOrMinusOne() {
            if (this.sapAncestor == -1) {
                return -1;
            }
            return getSapLength();
        }

        public void setSapLength(int sapLength) {
            this.sapLength = sapLength;
        }

        public int getSapAncestor() {
            return sapAncestor;
        }

        public int getSapLength() {
            return sapLength;
        }
    }
}

