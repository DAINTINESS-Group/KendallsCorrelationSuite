package tileBasedKendallAlgorithms;

import org.apache.spark.sql.AnalysisException;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import tileBasedKendallAlgorithms.algo.TileBasedCalculatorService;
import tileBasedKendallAlgorithms.reader.IDatasetReaderFactory;
import tileBasedKendallAlgorithms.sparkSetup.SparkSetup;

public class SparkBasedKendallManager {
    private final IDatasetReaderFactory datasetReaderFactory;
    private Dataset<Row> dataset;

    public SparkBasedKendallManager() {
        SparkSession sparkSession = initializeSparkSession();
        this.datasetReaderFactory = initializeDatasetReaderFactory(sparkSession);
    }

    private SparkSession initializeSparkSession() {
        SparkSetup sparkSetup = new SparkSetup();
        return sparkSetup.setup();
    }

    private IDatasetReaderFactory initializeDatasetReaderFactory(SparkSession sparkSession) {
        return new IDatasetReaderFactory(sparkSession);
    }

    public void loadDataset(String filePath, String column1, String column2) throws AnalysisException {
        this.dataset = datasetReaderFactory.createDatasetReader(filePath).read(column1, column2);
    }

    public double calculateKendallTau(String column1, String column2) {
        TileBasedCalculatorService calculatorService = new TileBasedCalculatorService(dataset, column1, column2);
        return calculatorService.calculateKendallTauCorrelation();
    }
}
