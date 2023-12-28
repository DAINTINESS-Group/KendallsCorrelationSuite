package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Tile implements Serializable {

    Range rangeX;
    Range rangeY;

    private final List<Long> pairIds;

    public Tile(Range rangeX, Range rangeY) {
        this.rangeX = rangeX;
        this.rangeY = rangeY;
        this.pairIds = new ArrayList<>();
    }

    public void addPairId(long pairId) {
        pairIds.add(pairId);
    }

    public boolean containsPairId(long pairId) {
        return pairIds.contains(pairId);
    }

    public boolean isEmpty() {
        return pairIds.isEmpty();
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
