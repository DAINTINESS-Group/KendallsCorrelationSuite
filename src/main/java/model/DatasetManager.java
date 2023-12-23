package model;

import kendall.BinCalculatorFactory;
import kendall.BinCalculatorFactory.BinCalculatorMethods;
import kendall.TileMethodCalculator;
import kendall.IBinCalculator;
import org.apache.spark.sql.AnalysisException;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import reader.IDatasetReaderFactory;

public class DatasetManager {
    private Dataset<Row> dataset;
    private final IDatasetReaderFactory datasetReaderFactory;
    private final BinCalculatorFactory binFactory = new BinCalculatorFactory();

    public DatasetManager() {
        SparkSetup sparkSetup = new SparkSetup();
        SparkSession spark = sparkSetup.setup();
        datasetReaderFactory = new IDatasetReaderFactory(spark);
    }

    public void registerDataset(String path) throws AnalysisException {
        dataset = datasetReaderFactory.createDatasetReader(path).read();

    }

    public double calculateKendall(String column1, String column2, BinCalculatorMethods binCalculationMethod) {
        IBinCalculator binCalculator = binFactory.createBinCalculator(binCalculationMethod);
        TileMethodCalculator tileMethodCalculator = new TileMethodCalculator(binCalculator);
        return tileMethodCalculator.calculateKendall(dataset, column1, column2);
    }
}
