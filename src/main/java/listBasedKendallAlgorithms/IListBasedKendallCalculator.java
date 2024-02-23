package listBasedKendallAlgorithms;

public interface IListBasedKendallCalculator {
    /**
     * Calculate Kendall's tau given a pair of columns
     *
     * @param pair Contains the column pair selected
     * @return Kendall's tau
     */
    double calculateKendall(ColumnPair pair);
}
