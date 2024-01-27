package newTryTiles;

import org.apache.spark.sql.AnalysisException;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import newTryTiles.algo.BinCalculatorFactory;
import newTryTiles.algo.IBinCalculator;
import newTryTiles.algo.TileBasedCalculatorService;
import newTryTiles.reader.IDatasetReaderFactory;
import newTryTiles.sparkSetup.SparkSetup;

public class SparkBasedKendallManager {
    private Dataset<Row> dataset;
    private final IDatasetReaderFactory datasetReaderFactory;
    private final BinCalculatorFactory binCalculatorFactory;

    public SparkBasedKendallManager() {
        SparkSession sparkSession = initializeSparkSession();
        this.datasetReaderFactory = initializeDatasetReaderFactory(sparkSession);
        this.binCalculatorFactory = new BinCalculatorFactory();
    }

    private SparkSession initializeSparkSession() {
        SparkSetup sparkSetup = new SparkSetup();
        return sparkSetup.setup();
    }

    private IDatasetReaderFactory initializeDatasetReaderFactory(SparkSession sparkSession) {
        return new IDatasetReaderFactory(sparkSession);
    }

    public void loadDataset(String filePath) throws AnalysisException {
        this.dataset = datasetReaderFactory.createDatasetReader(filePath).read();
    }

    public double calculateKendallTau(String column1, String column2, BinCalculatorFactory.BinCalculatorMethods calculationMethod) {
        IBinCalculator binCalculator = binCalculatorFactory.createBinCalculator(calculationMethod);
        TileBasedCalculatorService calculatorService = new TileBasedCalculatorService(dataset, binCalculator, column1, column2);
        return calculatorService.calculateKendallTauCorrelation();
    }
}
