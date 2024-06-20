package tiles.dom;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class TileInMemWCounters implements Serializable, ITile{

    private static final long serialVersionUID = 4581980665140154578L;
	private final int row;
    private final int column;
    private List<DoublePair> valuePairs;
    private long pairCounter = 0L;
    private SortedMap<Double,Integer> occurencesPerX;
    private SortedMap<Double,Integer> occurencesPerY;
    
    public TileInMemWCounters(int row, int column) {
        this.valuePairs = new ArrayList<>();
        this.row = row;
        this.column = column;
        this.occurencesPerX = new TreeMap<Double,Integer>();
        this.occurencesPerY = new TreeMap<Double,Integer>();
    }

    @Override
	public boolean isEmpty() {
        return pairCounter == 0;
    }

    @Override
	public long getCount() {
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

        //get the pair's X and Y values and increase the respective occurrence maps by 1
        //Syntax: merge(key,paramValue,agg(paramValue))
        this.occurencesPerX.merge(pair.getX(), 1, Integer::sum);
        this.occurencesPerY.merge(pair.getY(), 1, Integer::sum);
    }

    @Override
	public int getRow() {
        return row;
    }

    @Override
	public int getColumn() {
        return column;
    }

    public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public SortedMap<Double, Integer> getOccurencesPerX() {
		return occurencesPerX;
	}

	@Override
	public SortedMap<Double, Integer> getOccurencesPerY() {
		return occurencesPerY;
	}


	@Override
	public int setValuePairs(List<DoublePair> pairList) {
		this.valuePairs = pairList;
		return this.valuePairs.size();
	}
	
	@Override
    public String toString() {
//        return "TileInMemSimple{" +
//                "row=" + row +
//                ", column=" + column +
//                ", pairCounter=" + pairCounter +
//                '}';
//    }
//
//    public String toStringDetailed() {
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
}
