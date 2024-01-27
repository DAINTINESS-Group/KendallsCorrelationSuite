package newTryTiles.tiles;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.math3.util.Pair;

public class Tile implements Serializable {

    private static final long serialVersionUID = 1L;

	private Range rangeX;
    private Range rangeY;
    private int row;
    private int col;
    private final Set<Long> pairIds;
    private Set<Pair<Double, Double>> valuePairs;
    private long idCounter = 0;

    public Tile(Range rangeX, Range rangeY) {
        this.rangeX = rangeX;
        this.rangeY = rangeY;
        this.pairIds = new HashSet<>();
        this.valuePairs = new HashSet<>();
    }


    public boolean isEmpty() {
        return valuePairs.isEmpty();
    }

    public long getCount() {
        return idCounter;
    }

    
    public Set<Pair<Double, Double>> getValuePairs() {
        return valuePairs;
    }

    public void addValuePair(Pair<Double, Double> pair) {
        valuePairs.add(pair);
        idCounter++;
    }

    public void clearPairsList() {
        valuePairs.clear();
    }

    @Deprecated
    public Set<Long> getPairIds() {
        return pairIds;
    }
    @Deprecated
    public void addPairId(long pairId) {
        pairIds.add(pairId);
        idCounter++;
    }
    @Deprecated
    public void clearIdList() {
        pairIds.clear();
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

    @Override
    public String toString() {
        return "Tile { " +
                "X:[" + rangeX.getStart() + " - " + rangeX.getEnd() + ") " +
                "Y:[" + rangeY.getStart() + " - " + rangeY.getEnd()  + ")" +
                " -> Ids=" + pairIds +
                " }";
    }
}
