package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Tile implements Serializable {
    private final double startX;
    private final double endX;
    private final double startY;
    private final double endY;
    private final List<Long> pairIds;

    public Tile(double startX, double endX, double startY, double endY) {
        this.startX = startX;
        this.endX = endX;
        this.startY = startY;
        this.endY = endY;
        this.pairIds = new ArrayList<>();
    }

    public void addPairId(long pairId) {
        pairIds.add(pairId);
    }

    public boolean isEmpty() {
        return pairIds.isEmpty();
    }

    @Override
    public String toString() {
        return "Tile { " +
                "X:[" + startX + " - " + endX + "]" +
                "Y:[" + startY + " - " + endY  + "]" +
                " -> Ids=" + pairIds +
                " }";
    }
}
