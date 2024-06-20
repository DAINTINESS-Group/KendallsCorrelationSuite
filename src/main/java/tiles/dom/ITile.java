package tiles.dom;

import java.util.List;
import java.util.SortedMap;

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
	public long getCount();

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
	 * completely renews the contents of a tile with a pre-made List of double pairs
	 * 
	 * @param pairList a List of Double Pairs
	 * @return the size of the tile's contents
	 */
	public int setValuePairs(List<DoublePair> pairList);
	
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