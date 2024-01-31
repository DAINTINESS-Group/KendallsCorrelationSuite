package tileBasedKendallAlgorithms.tiles;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.util.Pair;

public class Tile {

    private int row;
    private int col;
    private final List<Pair<Double, Double>> valuePairs;
    private double pairCounter = 0;

    public Tile() {
        this.valuePairs = new ArrayList<>();
    }

    public boolean isEmpty() {
        return pairCounter == 0;
    }

    public double getCount() {
        return pairCounter;
    }

    public List<Pair<Double, Double>> getValuePairs() {
        return valuePairs;
    }

    public void addValuePair(Pair<Double, Double> pair) {
        valuePairs.add(pair);
        pairCounter++;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
}
