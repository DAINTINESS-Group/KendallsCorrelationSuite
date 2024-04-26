package util.tileband;

import java.util.SortedMap;
import java.util.TreeMap;

public class TileBandSimple {
	public enum BandType{ ROW, COLUMN }

	protected BandType type;
	protected Integer position;
	protected SortedMap<Double,Integer> occurencesPerValue;
	
	public TileBandSimple(BandType type, Integer position) {
		super();
		this.type = type;
		this.position = position;
		this.occurencesPerValue = new TreeMap<Double,Integer>();
	}

	public BandType getType() {
		return type;
	}

	public Integer getPosition() {
		return position;
	}

	public SortedMap<Double, Integer> getOccurencesPerValue() {
		return occurencesPerValue;
	}

	@Override
	public String toString() {
		return type + "\t" + position + "\t" + occurencesPerValue.values().toString();
	}
}//end class
