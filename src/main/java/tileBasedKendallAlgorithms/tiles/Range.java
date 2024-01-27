package tileBasedKendallAlgorithms.tiles;

import java.io.Serializable;

public class Range implements Serializable {
    private static final long serialVersionUID = 1032137880150440250L;
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
