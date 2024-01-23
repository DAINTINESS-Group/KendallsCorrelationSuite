package tileBasedKendallAlgorithms.algo;

import java.util.Objects;

/**
 * This class is used to store pairs of ids that are stored in a HashSet
 * in order to check whether a set of ids has been compared in the past
 */
public class Pair {
    private final Long id1;
    private final Long id2;

    public Pair(Long id1, Long id2) {
        this.id1 = Math.min(id1, id2); // Ensure consistent ordering
        this.id2 = Math.max(id1, id2);
    }

    @Override
    public boolean equals(Object otherPair) {
        if (this == otherPair) return true;
        if (!(otherPair instanceof Pair)) return false;
        Pair pair = (Pair) otherPair;

        return id1.equals(pair.id1) && id2.equals(pair.id2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id1, id2);
    }
}
