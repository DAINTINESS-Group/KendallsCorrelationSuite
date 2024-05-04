package client;

//import listBasedKendallAlgorithms.IListBasedKendallCalculator;
//import listBasedKendallAlgorithms.IListBasedKendallFactory;
import listBasedKendallAlgorithms.*;
import listBasedKendallAlgorithms.reader.ColumnPair;
import listBasedKendallAlgorithms.reader.Reader;
import sparkBasedKendallAlgorithms.TilesWithSimplePointChecksSparkReaderStoredTilesKendallCalculator;
import sparkBasedKendallAlgorithms.TilesWithSimplePointChecksSparkReaderKendallCalculator;
import util.writer.WriterSetup;

import org.apache.spark.sql.AnalysisException;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

@SuppressWarnings("unused")
public class ClientV1_ApacheVsSpark {
    public static void main(String[] args) throws IOException, AnalysisException {

        //74001 tuples
        String filePath = "src\\test\\resources\\input\\acs2017_census_tract_data.csv";
        String column1 = "Hispanic";
        String column2 = "Native";
        String delimiter = ",";

        //74001 tuples
//        String filePath = "src\\test\\resources\\input\\acs2017_census_tract_data.csv";
//        String column1 = "Men";
//        String column2 = "Women";
//        String delimiter = ",";
        
        //108,539 tuples CARS_100K
        // manufacturer,model,year,price,transmission,mileage,fuelType,tax,mpg,engineSize
//        String filePath = "src\\test\\resources\\input\\cars_100k.csv";
//        String column1 = "mileage";
//        String column2 = "mpg";
//        String delimiter = ",";
        
      //619,040 tuples
//        String filePath = "src\\test\\resources\\input\\all_stocks_5yr.csv";
//        String column1 = "low";
//        String column2 = "high";
//        String delimiter = ",";
        
        //619,040 tuples
//        String filePath = "src\\test\\resources\\input\\all_stocks_5yr.csv";
//        String column1 = "close";
//        String column2 = "volume";
//        String delimiter = ",";

        //1,000,000 = 1 million tuples
//        String filePath = "src\\test\\resources\\input\\Random1Mil.csv";
//        String column1 = "X";
//        String column2 = "Y";
//        String delimiter = ",";
        
        //5,819,079 tuples
//        String filePath = "src\\test\\resources\\input\\flights5_7m.csv"; // flight cancels 2015
//        String column1 = "FLIGHT_NUMBER";
//        String column2 = "DEPARTURE_TIME";
//        String delimiter = ",";

        // Get the Java runtime

        checkMemory();
        long startTime = -1;
        long endTime = -1;
        double elapsedTimeSeconds = -1.0;

        IListBasedKendallFactory methods = new IListBasedKendallFactory();
        startTime = System.currentTimeMillis();
        Reader reader = new Reader();
        ColumnPair columnPair = reader.read(filePath, column1, column2, delimiter);
        endTime = System.currentTimeMillis();
        elapsedTimeSeconds = (endTime - startTime) / 1000.0;
        printResults("ValueReader For ALL lists: ", filePath, Double.NaN, elapsedTimeSeconds);

        /* APACHE */
        startTime = System.currentTimeMillis();
        IListBasedKendallCalculator apacheKendall = methods.createKendallCalculatorByString("Apache kendall");
        double apacheResult = apacheKendall.calculateKendall(columnPair);
        endTime = System.currentTimeMillis();
        elapsedTimeSeconds = (endTime - startTime) / 1000.0;
        printResults("Apache", filePath, apacheResult, elapsedTimeSeconds);
        checkMemory();
        
        /* Tile Implementation with SPARK and valuePairs*/
//        startTime = System.currentTimeMillis();
//        TilesWithSimplePointChecksSparkReaderKendallCalculator sparkBasedKendallManagerSimple = new TilesWithSimplePointChecksSparkReaderKendallCalculator();
//        sparkBasedKendallManagerSimple.loadDataset(filePath, column1, column2);
//        endTime = System.currentTimeMillis();
//        elapsedTimeSeconds = (endTime - startTime) / 1000.0;
//        System.out.println("Spark InitialSetup and Dataset loading took: " + elapsedTimeSeconds + "\n");
//
//        startTime = System.currentTimeMillis();
//        double sparkTileKendallResult = sparkBasedKendallManagerSimple.calculateKendallTau(column1, column2);
//        endTime = System.currentTimeMillis();
//        elapsedTimeSeconds = (endTime - startTime) / 1000.0;
//        printResults("Spark Tiles", filePath, sparkTileKendallResult, elapsedTimeSeconds);


        /* Tile Implementation with SPARK and stored tiles*/
        
        /* Produces an out of memory error with many tiles (e.g., via Scott's rule)
         * as it opens one filewriter per tile.
         * And we have thousands of tiles. 
         * If we fix the number of tiles to 100x100, it uses o(150MB) and works*/
        startTime = System.currentTimeMillis();        
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
        WriterSetup.OUTPUT_CUR_DIR = "try" + timeStamp + File.separator;
        System.err.println("Writing tiles at " + WriterSetup.getOutputExecDir());
        
        TilesWithSimplePointChecksSparkReaderStoredTilesKendallCalculator tilesWithSimplePointChecksSparkReaderStoredTilesKendallCalculator= new TilesWithSimplePointChecksSparkReaderStoredTilesKendallCalculator();
        tilesWithSimplePointChecksSparkReaderStoredTilesKendallCalculator.loadDataset(filePath, column1, column2);
        endTime = System.currentTimeMillis();
        elapsedTimeSeconds = (endTime - startTime) / 1000.0;
        System.out.println("Spark InitialSetup and Dataset loading took: " + elapsedTimeSeconds + "\n");

        startTime = System.currentTimeMillis();
        double sparkTileKendallResult = tilesWithSimplePointChecksSparkReaderStoredTilesKendallCalculator.calculateKendallTau(column1, column2);
        endTime = System.currentTimeMillis();
        elapsedTimeSeconds = (endTime - startTime) / 1000.0;
        printResults("Spark: Simple Structure + Stored Tiles", filePath, sparkTileKendallResult, elapsedTimeSeconds);
       
        Thread deleteThread = new Thread(() -> {
        	boolean deletionFlag = tilesWithSimplePointChecksSparkReaderStoredTilesKendallCalculator.deleteSubFolders(new File(WriterSetup.getOutputExecDir()));
            System.err.println("Cleanup of tiles at " + WriterSetup.getOutputExecDir() + " was " + deletionFlag);
        });
        deleteThread.start();
        
        checkMemory();
        
    }//end main

    private static void printResults(String methodName, String filePath, double kendallResult, double elapsedTimeSeconds) {
		// Print the result
    	System.out.println("\n\n" + " ----- ");
        System.out.println(methodName + " method for file " + filePath);
        System.out.println(methodName + " Kendall tau value:\t" + kendallResult);
        System.out.println(methodName+" elapsed time (sec):\t" + elapsedTimeSeconds + " seconds");
        System.out.println(" ----- \n");
	}
    
    private static void checkMemory() {
    	System.out.println("Calling garbageCollector");
        Runtime runtime = Runtime.getRuntime();
        runtime.gc();
    	long total = runtime.totalMemory();
    	long free = runtime.freeMemory();
    	long usedMemory= total - free;
        System.out.println("Used/Free/Total Memory (MB): "+ bytesToMegabytes(usedMemory) + 
        		"\t" + bytesToMegabytes(free) + "\t" + bytesToMegabytes(total));
    }
    

    public static long bytesToMegabytes(long bytes) {
    	final long MEGABYTE = 1024L * 1024L;
    	return bytes / MEGABYTE;
    }
    
}//end class