package sparkBasedKendallAlgorithms;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.spark.sql.AnalysisException;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import sparkBasedKendallAlgorithms.reader.IDatasetReaderFactory;
import sparkBasedKendallAlgorithms.sparkSetup.SparkSetup;
import tiles.algos.AlgoSimpleTilesAndPointComparison;
import tiles.algos.CalculationTimer;
import tiles.algos.CorrelationStatistics;
import tiles.dom.TileStoredSimple;
import tiles.dom.writer.WriterSetup;
import tiles.tilemgr.TilesManagerSparkReaderTilesStoredSimple;

/**
 * Tiles: Simple (InMem with Simple structure)
 * TilesManager: SparkReader _  Stored Tiles
 * Tiles Processing Algo: Simple points and checks
 * 
 * 
 * @author pvassil
 *
 */

public class TilesWithSimplePointChecksSparkReaderStoredTilesKendallCalculator {
	protected static final boolean DEBUG_FLAG = false;
    private Dataset<Row> dataset;
    private final SparkSession sparkSession;

    public TilesWithSimplePointChecksSparkReaderStoredTilesKendallCalculator() {
        SparkSetup sparkSetup = new SparkSetup();
        sparkSession = sparkSetup.setup();
        
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
        WriterSetup.OUTPUT_CUR_DIR = "try" + timeStamp + File.separator;
        System.err.println("Writing tiles at " + WriterSetup.getOutputExecDir());
    }

    public void loadDataset(String filePath, String column1, String column2) throws AnalysisException {
        IDatasetReaderFactory datasetReaderFactory = new IDatasetReaderFactory(sparkSession);
        dataset = datasetReaderFactory.createDatasetReader(filePath).read(column1, column2);
    }

    public double calculateKendallTau(String column1, String column2) {
        TilesManagerSparkReaderTilesStoredSimple tilesManager = new TilesManagerSparkReaderTilesStoredSimple(dataset, column1, column2);
//        TileStoredBasedCalculatorService calculatorService = new TileStoredBasedCalculatorService(tilesManager);
//        return calculatorService.calculateKendallTauCorrelation();
//    }
//    
	CorrelationStatistics statistics = new CorrelationStatistics();
	CalculationTimer timer = new CalculationTimer();

	TileStoredSimple[][] tiles = tilesManager.createTilesArray();

	AlgoSimpleTilesAndPointComparison processorSimpleSpark = new AlgoSimpleTilesAndPointComparison(tiles, statistics);
	processorSimpleSpark.processAllTiles();

	if(DEBUG_FLAG) {
		System.out.println(statistics);
		System.out.println(timer);
	}
	return statistics.calculateCorrelationResult();
}
    
    public boolean deleteSubFolders(File folder) {
    	String path = folder.getAbsolutePath();  
    	System.err.println("Cleaning up folder " + path);
    	boolean deletionFlag = false;
        if (folder.exists()) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        deleteSubFolders(file);
                    }
                    file.delete();
                }
            }
            deletionFlag = folder.delete();
        }//end if
        return deletionFlag;
    }//end delete()
}
