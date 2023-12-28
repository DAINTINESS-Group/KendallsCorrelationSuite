package tileBasedKendallAlgorithms.tiles;

import java.io.Serializable;

public class Range implements Serializable {
    private final double start;
    private final double end;

    public Range(double start, double end){
        this.start = start;
        this.end = end;
    }

    public double getStart() {
        return start;
    }

    public double getEnd() {
        return end;
    }
}
