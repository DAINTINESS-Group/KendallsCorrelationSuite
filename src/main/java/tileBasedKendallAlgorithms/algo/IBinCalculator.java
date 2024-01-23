package tileBasedKendallAlgorithms.algo;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

/**
 * The IBinCalculator interface defines a method for calculating the optimal number of bins
 * for a given column in a dataset. This calculation can be based on various statistical methods
 * and heuristics to determine the most appropriate binning strategy for the data.
 * Implementations of this interface should provide specific algorithms for bin calculation
 * considering the distribution and characteristics of the dataset.
 */
public interface IBinCalculator {

    /**
     * Calculates the number of bins for a specified column within a dataset.
     * @param dataset The dataset containing the data to be binned.
     * @param columnName The name of the column for which bins are to be calculated.
     * @return The calculated number of bins for the specified column. The return value should
     *         be a positive integer indicating the optimal number of bins, based on the implementation's
     *         algorithm and analysis of the column data.
     */
    public int calculateBins(Dataset<Row> dataset, String columnName);
}
