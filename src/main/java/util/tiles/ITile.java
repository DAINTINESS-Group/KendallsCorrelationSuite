package util.tiles;

import java.util.List;

import util.common.DoublePair;

public interface ITile {

	boolean isEmpty();

	int getCount();

	List<DoublePair> getValuePairs();

	void addValuePair(DoublePair pair);

	int getRow();

	int getColumn();

	String toString();

}