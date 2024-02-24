package listBasedKendallAlgorithms.listBasedReader;

import java.util.ArrayList;

public class ColumnPair {
    private final ArrayList<Double> x;
    private final ArrayList<Double> y;

    public ColumnPair() {
        this.x = new ArrayList<>();
        this.y = new ArrayList<>();
    }

    public void addX(double value) {
        x.add(value);
    }

    public void addY(double value) {
        y.add(value);
    }

    public ArrayList<Double> getXColumn() {
        return x;
    }

    public ArrayList<Double> getYColumn() {
        return y;
    }
}
