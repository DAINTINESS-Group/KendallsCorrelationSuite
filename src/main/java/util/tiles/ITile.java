package util.tiles;

import java.util.List;
import java.util.SortedMap;

import util.common.DoublePair;

public interface ITile {

	/**
	 * 
	 * @return
	 */
	public boolean isEmpty();

	/**
	 * 
	 * @return
	 */
	public int getCount();

	/**
	 * 
	 * @return
	 */
	public List<DoublePair> getValuePairs();

	/**
	 * 
	 * @param pair
	 */
	public void addValuePair(DoublePair pair);

	/**
	 * 
	 * @return
	 */
	public int getRow();

	/**
	 * 
	 * @return
	 */
	public int getColumn();
	
	/**
	 * Returns null (if the method does not support counting #occcurrences per value or a sorted map otherwise
	 * @return null if there are no value maps, sorted maps of pairs <value, how many times it occurs in the map>
	 */
	public SortedMap<Double, Integer> getOccurencesPerX();

	/**
	 * Returns null (if the method does not support counting #occcurrences per value or a sorted map otherwise
	 * @return null if there are no value maps, sorted maps of pairs <value, how many times it occurs in the map>
	 */
	public SortedMap<Double, Integer> getOccurencesPerY();

	/**
	 * Returns a string that describes the tile
	 * 
	 * @return a string that describes the tile
	 */
	public String toString();

}