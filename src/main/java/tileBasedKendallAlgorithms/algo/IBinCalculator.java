package tileBasedKendallAlgorithms.algo;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

public interface IBinCalculator {

    public int calculateNumberOfBins(Dataset<Row> dataset, String columnName);
}
