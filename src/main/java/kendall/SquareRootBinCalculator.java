package kendall;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

public class SquareRootBinCalculator implements IBinCalculator {
    @Override
    public int calculateNumberOfBins(Dataset<Row> dataset, String columnName) {
        double sampleSize = dataset.select(columnName).count();
        return (int) Math.ceil(Math.sqrt(sampleSize));
    }
}