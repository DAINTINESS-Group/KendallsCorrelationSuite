package sparkBasedKendallAlgorithms;

import org.apache.spark.sql.AnalysisException;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import sparkBasedKendallAlgorithms.reader.IDatasetReaderFactory;
import sparkBasedKendallAlgorithms.sparkSetup.SparkSetup;
import util.tilemgr.TilesManagerSparkReaderTilesStoredSimple;
import util.algo.TileStoredBasedCalculatorService;

public class SparkBasedKendallManager {
    private Dataset<Row> dataset;
    private final SparkSession sparkSession;

    public SparkBasedKendallManager() {
        SparkSetup sparkSetup = new SparkSetup();
        sparkSession = sparkSetup.setup();
    }

    public void loadDataset(String filePath, String column1, String column2) throws AnalysisException {
        IDatasetReaderFactory datasetReaderFactory = new IDatasetReaderFactory(sparkSession);
        dataset = datasetReaderFactory.createDatasetReader(filePath).read(column1, column2);
    }

    public double calculateKendallTau(String column1, String column2) {
        TilesManagerSparkReaderTilesStoredSimple tilesManagerSparkReaderTilesStoredSimple = new TilesManagerSparkReaderTilesStoredSimple(dataset, column1, column2);
        TileStoredBasedCalculatorService calculatorService = new TileStoredBasedCalculatorService(tilesManagerSparkReaderTilesStoredSimple);
        return calculatorService.calculateKendallTauCorrelation();
    }
}
