package tileBasedKendallAlgorithms.sparkSetup;

import org.apache.spark.sql.AnalysisException;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import tileBasedKendallAlgorithms.algo.TileBasedCalculatorService;
import tileBasedKendallAlgorithms.reader.IDatasetReaderFactory;

public class SparkBasedKendallManager {
    private Dataset<Row> dataset;
    SparkSession sparkSession;

    public SparkBasedKendallManager() {
        SparkSetup sparkSetup = new SparkSetup();
        sparkSession = sparkSetup.setup();
    }

    public void loadDataset(String filePath, String column1, String column2) throws AnalysisException {
        IDatasetReaderFactory datasetReaderFactory = new IDatasetReaderFactory(sparkSession);
        dataset = datasetReaderFactory.createDatasetReader(filePath).read(column1, column2);
    }

    public double calculateKendallTau(String column1, String column2) {
        TileBasedCalculatorService calculatorService = new TileBasedCalculatorService(dataset, column1, column2);
        return calculatorService.calculateKendallTauCorrelation();
    }
}
