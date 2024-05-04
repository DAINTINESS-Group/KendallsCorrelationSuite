package sparkBasedKendallAlgorithms;

import org.apache.spark.sql.AnalysisException;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import sparkBasedKendallAlgorithms.reader.IDatasetReaderFactory;
import sparkBasedKendallAlgorithms.sparkSetup.SparkSetup;
import tiles.algos.AlgoSimpleTilesAndPointComparison;
import tiles.algos.CalculationTimer;
import tiles.algos.CorrelationStatistics;
import tiles.dom.ITile;
import tiles.tilemgr.TilesManagerSparkReaderTilesInMemSimple;

/**
 * Tiles: Simple (InMem with Simple structure)
 * TilesManager: SparkReader _  Simple InMem tiles
 * Tiles Processing Algo: Simple points and checks
 * 
 * 
 * @author pvassil
 *
 */
public class TilesWithSimplePointChecksSparkReaderKendallCalculator {
	protected static final boolean DEBUG_FLAG = false;
    private Dataset<Row> dataset;
    private final SparkSession sparkSession;

    public TilesWithSimplePointChecksSparkReaderKendallCalculator() {
        SparkSetup sparkSetup = new SparkSetup();
        sparkSession = sparkSetup.setup();
    }

    public void loadDataset(String filePath, String column1, String column2) throws AnalysisException {
        IDatasetReaderFactory datasetReaderFactory = new IDatasetReaderFactory(sparkSession);
        dataset = datasetReaderFactory.createDatasetReader(filePath).read(column1, column2);
    }

    public double calculateKendallTau(String column1, String column2) {
        TilesManagerSparkReaderTilesInMemSimple tilesManager = new TilesManagerSparkReaderTilesInMemSimple(dataset, column1, column2);
//        TileBasedCalculatorService calculatorService = new TileBasedCalculatorService(tilesManagerSparkReaderTilesInMemSimple);
//        return calculatorService.calculateKendallTauCorrelation(AlgoEnum.SPARK_TILES_ALGO);
//    }
//    
  //public double calculateKendallTauCorrelation() {
  	CorrelationStatistics statistics = new CorrelationStatistics();
  	CalculationTimer timer = new CalculationTimer();

  	ITile[][] tiles = tilesManager.createTilesArray();


  		AlgoSimpleTilesAndPointComparison processorSimple = new AlgoSimpleTilesAndPointComparison(tiles, statistics);
  		processorSimple.processAllTiles();
  	if(DEBUG_FLAG) {
  		System.out.println(statistics);
  		System.out.println(timer);
  	}
  	return statistics.calculateCorrelationResult();
  }
    
    
}
