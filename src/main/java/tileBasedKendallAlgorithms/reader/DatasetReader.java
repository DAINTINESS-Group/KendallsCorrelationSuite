package tileBasedKendallAlgorithms.reader;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import static org.apache.spark.sql.functions.col;

public class DatasetReader implements IDatasetReader {

    private final SparkSession sparkSession;
    private final String path;
    private final String delimiter;

    public DatasetReader(SparkSession sparkSession, String path, String delimiter) {
        this.sparkSession = sparkSession;
        this.path = path;
        this.delimiter = delimiter;
    }

    public Dataset<Row> read(String column1, String column2) {
        return sparkSession
                .read()
                .option("header", true)
                .option("nullValue", "")
                .option("delimiter", delimiter)
                .csv(path)
                .select(column1, column2)
                .na().drop()
                .withColumn(column1, col(column1).cast("double"))
                .withColumn(column2, col(column2).cast("double"));
    }
}
