package util.tiles;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;

import util.common.DoublePair;

public class TileInMemSimple implements Serializable, ITile {

    private static final long serialVersionUID = 4581980665140154578L;
	private final int row;
    private final int column;
    private final List<DoublePair> valuePairs;
    private int pairCounter = 0;

    public TileInMemSimple(int row, int column) {
        this.valuePairs = new ArrayList<>();
        this.row = row;
        this.column = column;
    }

    @Override
	public boolean isEmpty() {
        return pairCounter == 0;
    }

    @Override
	public int getCount() {
        return pairCounter;
    }

    @Override
	public List<DoublePair> getValuePairs() {
        return valuePairs;
    }

    @Override
	public void addValuePair(DoublePair pair) {
        valuePairs.add(pair);
        pairCounter++;
    }

    @Override
	public int getRow() {
        return row;
    }

    @Override
	public int getColumn() {
        return column;
    }

    @Override
    public String toString() {
        return "TileInMemSimple{" +
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
    	
        return "TileInMemSimple{" +
                "row=" + row +
                ", column=" + column +
                ", pairs=" + vStr +
                '}';
    }

	@Override
	public SortedMap<Double, Integer> getOccurencesPerX() {
		return null;
	}

	@Override
	public SortedMap<Double, Integer> getOccurencesPerY() {
		return null;
	}
}
