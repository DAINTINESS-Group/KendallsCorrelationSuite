package newTryTiles.reader;

import org.apache.spark.sql.AnalysisException;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

/**
 * This interface defines the contract for a DatasetReader.
 * The reader reads a dataset .
 */
public interface IDatasetReader {

    /**
     * Reads data from a specified source and constructs a Dataset<Row> based on the data.
     *
     * @return A Dataset<Row> representing the data read from the source.
     * @throws AnalysisException if the data cannot be read or parsed correctly, or if
     *                           there are issues with the data source configuration. This exception
     *                           should be thrown to indicate any problems encountered during
     *                           the data reading process that prevent the creation of a valid Dataset.
     */
    Dataset<Row> read() throws AnalysisException;
}
