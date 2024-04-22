package util.tiles;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import util.common.DoublePair;

public class Tile implements Serializable {

    private static final long serialVersionUID = 4581980665140154578L;
	private final int row;
    private final int column;
    private final List<DoublePair> valuePairs;
    private double pairCounter = 0;

    public Tile(int row, int column) {
        this.valuePairs = new ArrayList<>();
        this.row = row;
        this.column = column;
    }

    public boolean isEmpty() {
        return pairCounter == 0;
    }

    public double getCount() {
        return pairCounter;
    }

    public List<DoublePair> getValuePairs() {
        return valuePairs;
    }

    public void addValuePair(DoublePair pair) {
        valuePairs.add(pair);
        pairCounter++;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    @Override
    public String toString() {
        return "Tile{" +
                "row=" + row +
                ", column=" + column +
                ", pairCounter=" + pairCounter +
                '}';
    }

    public String toStringDetailed() {
    	String vStr="";
    	for(DoublePair p: valuePairs) {
    		vStr = vStr + p.toString();
    	}
    	
        return "Tile{" +
                "row=" + row +
                ", column=" + column +
                ", pairs=" + vStr +
                '}';
    }
}
