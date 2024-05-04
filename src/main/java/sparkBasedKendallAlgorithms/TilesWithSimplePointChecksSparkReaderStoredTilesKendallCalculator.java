package sparkBasedKendallAlgorithms;

import java.io.File;

import org.apache.spark.sql.AnalysisException;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import sparkBasedKendallAlgorithms.reader.IDatasetReaderFactory;
import sparkBasedKendallAlgorithms.sparkSetup.SparkSetup;
import util.tilemgr.TilesManagerSparkReaderTilesStoredSimple;
import util.algo.TileStoredBasedCalculatorService;

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
    private Dataset<Row> dataset;
    private final SparkSession sparkSession;

    public TilesWithSimplePointChecksSparkReaderStoredTilesKendallCalculator() {
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
