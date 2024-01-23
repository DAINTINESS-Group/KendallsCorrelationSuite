package tileBasedKendallAlgorithms.tiles;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class Tile implements Serializable {

    private static final long serialVersionUID = 1L;

	Range rangeX;
    Range rangeY;

    private int row;
    private int col;

    private final Set<Long> pairIds;
    private long idCounter = 0;

    public Tile(Range rangeX, Range rangeY) {
        this.rangeX = rangeX;
        this.rangeY = rangeY;
        this.pairIds = new HashSet<>();
    }

    public void addPairId(long pairId) {
        pairIds.add(pairId);
        idCounter++;
    }

    public boolean isEmpty() {
        return pairIds.isEmpty();
    }

    public long getCount() {
        return idCounter;
    }

    public Set<Long> getPairIds() {
        return pairIds;
    }

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
