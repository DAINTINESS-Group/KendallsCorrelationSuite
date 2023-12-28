package tileBasedKendallAlgorithms;

import org.apache.spark.sql.AnalysisException;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import tileBasedKendallAlgorithms.algo.BinCalculatorFactory;
import tileBasedKendallAlgorithms.algo.IBinCalculator;
import tileBasedKendallAlgorithms.algo.TileBasedCalculatorService;
import tileBasedKendallAlgorithms.algo.BinCalculatorFactory.BinCalculatorMethods;
import tileBasedKendallAlgorithms.reader.IDatasetReaderFactory;
import tileBasedKendallAlgorithms.sparkSetup.SparkSetup;

public class SparkBasedKendallManager {
    private Dataset<Row> dataset;
    private final IDatasetReaderFactory datasetReaderFactory;
    private final BinCalculatorFactory binFactory = new BinCalculatorFactory();

    public SparkBasedKendallManager() {
        SparkSetup sparkSetup = new SparkSetup();
        SparkSession spark = sparkSetup.setup();
        datasetReaderFactory = new IDatasetReaderFactory(spark);
    }

    public Dataset<Row> registerDataset(String path) throws AnalysisException {
    	return dataset = datasetReaderFactory.createDatasetReader(path).read();
    }

    public double calculateKendall(String column1, String column2, BinCalculatorMethods binCalculationMethod) {
        IBinCalculator binCalculator = binFactory.createBinCalculator(binCalculationMethod);
        TileBasedCalculatorService tileBasedCalculatorService = new TileBasedCalculatorService(binCalculator);
        return tileBasedCalculatorService.calculateKendall(dataset, column1, column2);
    }
}
