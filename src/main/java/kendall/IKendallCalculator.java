package kendall;

import model.ColumnPair;

/**
 * TODO: complete the JavaDoc!!!
 * 
 * @author pvassil
 *
 */
public interface IKendallCalculator {
	/**
	 * Calculate Kendall's tau given a pair of columns
	 * @param pair Contains the column pair selected
	 * @return Kendall's tau
	 */
    public double calculateKendall(ColumnPair pair);
}
