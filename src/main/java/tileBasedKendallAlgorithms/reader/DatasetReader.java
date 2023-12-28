package tileBasedKendallAlgorithms.reader;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

public class DatasetReader implements IDatasetReader {

    private final SparkSession sparkSession;
    private final String path;
    private final String delimiter;

    public DatasetReader(SparkSession sparkSession, String path, String delimiter) {
        this.sparkSession = sparkSession;
        this.path = path;
        this.delimiter = delimiter;
    }

    public Dataset<Row> read() {
        return sparkSession
                .read()
                .option("header", true)
                .option("nullValue", "")
                .option("delimiter", delimiter)
                .csv(path)
                .na().drop();
    }
}
