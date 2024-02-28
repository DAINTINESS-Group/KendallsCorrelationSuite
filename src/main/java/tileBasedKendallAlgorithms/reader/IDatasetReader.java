package tileBasedKendallAlgorithms.reader;

import org.apache.spark.sql.AnalysisException;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

/**
 * This interface defines the contract for a DatasetReader.
 */
public interface IDatasetReader {

    /**
     * Reads data from a specified source and constructs a Dataset<Row> based on the data.
     *
     * @param column1 First column to be calculated
     * @param column2 Second column to be calculated
     * @return A Dataset<Row> representing the data read from the source.
     * @throws AnalysisException if the data cannot be read or parsed correctly, or if
     *                           there are issues with the data source configuration. This exception
     *                           should be thrown to indicate any problems encountered during
     *                           the data reading process that prevent the creation of a valid Dataset.
     */
    Dataset<Row> read(String column1, String column2) throws AnalysisException;
}
