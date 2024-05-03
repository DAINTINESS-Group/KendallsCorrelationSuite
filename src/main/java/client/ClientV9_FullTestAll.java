package client;

import listBasedKendallAlgorithms.*;
import listBasedKendallAlgorithms.listBasedReader.ColumnPair;
import listBasedKendallAlgorithms.listBasedReader.Reader;
import sparkBasedKendallAlgorithms.SparkBasedKendallManager;
import sparkBasedKendallAlgorithms.SparkBasedKendallManagerSimple;
import util.writer.WriterSetup;

import org.apache.spark.sql.AnalysisException;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ClientV9_FullTestAll {
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
        
        //108,539 tuples
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


        long startTime = -1;
        long endTime = -1;
        double elapsedTimeSeconds = -1.0;

        ListBasedKendallFactory methods = new ListBasedKendallFactory();
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

        /* TILES WITH LISTS*/
        startTime = System.currentTimeMillis();
        IListBasedKendallCalculator lbtbMgr = methods.createKendallCalculatorByString("ListBasedTiles");
        double listTileKendallResult =lbtbMgr.calculateKendall(columnPair);
        endTime = System.currentTimeMillis();
        elapsedTimeSeconds = (endTime - startTime) / 1000.0; 
        printResults("List Tiles", filePath, listTileKendallResult, elapsedTimeSeconds);
        
        /* TILES WITH MEMORY*/
        startTime = System.currentTimeMillis();
        IListBasedKendallCalculator bwmMgr = methods.createKendallCalculatorByString("BandsWithMemory");
        double bandsWithMemoryKendallResult =bwmMgr.calculateKendall(columnPair);
        endTime = System.currentTimeMillis();
        elapsedTimeSeconds = (endTime - startTime) / 1000.0; 
        printResults("Bands With Memory", filePath, bandsWithMemoryKendallResult, elapsedTimeSeconds);
        
        /* BRUTE */
        IListBasedKendallCalculator bruteForceTauA = methods.createKendallCalculatorByString("BruteForce");
        startTime = System.currentTimeMillis();
        double actualBruteForce = bruteForceTauA.calculateKendall(columnPair);
        endTime = System.currentTimeMillis();
        elapsedTimeSeconds = (endTime - startTime) / 1000.0;
        printResults("Brute Force", filePath, actualBruteForce, elapsedTimeSeconds);      

        /* BROPHY */
        IListBasedKendallCalculator brophyKendallTauB = methods.createKendallCalculatorByString("Brophy");
        startTime = System.currentTimeMillis();
        double actualBrophy = brophyKendallTauB.calculateKendall(columnPair);
        endTime = System.currentTimeMillis();
        elapsedTimeSeconds = (endTime - startTime) / 1000.0;
        printResults("Brophy", filePath, actualBrophy, elapsedTimeSeconds);
        
        /* Tile Implementation with SPARK and valuePairs*/
        startTime = System.currentTimeMillis();
        SparkBasedKendallManagerSimple sparkBasedKendallManagerSimple = new SparkBasedKendallManagerSimple();
        sparkBasedKendallManagerSimple.loadDataset(filePath, column1, column2);
        endTime = System.currentTimeMillis();
        elapsedTimeSeconds = (endTime - startTime) / 1000.0;
        System.out.println("Spark InitialSetup and Dataset loading took: " + elapsedTimeSeconds + "\n");

        startTime = System.currentTimeMillis();
        double sparkKendall = sparkBasedKendallManagerSimple.calculateKendallTau(column1, column2);
        endTime = System.currentTimeMillis();
        elapsedTimeSeconds = (endTime - startTime) / 1000.0;
        printResults("Spark w. Simple InMem Tiles", filePath, sparkKendall, elapsedTimeSeconds);
        
        
        /* Tile Implementation with SPARK and stored tiles*/
        startTime = System.currentTimeMillis();        
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
        WriterSetup.OUTPUT_CUR_DIR = "try" + timeStamp + File.separator;
        System.err.println("Writing tiles at " + WriterSetup.getOutputExecDir());
        
        SparkBasedKendallManager sparkBasedKendallManager= new SparkBasedKendallManager();
        sparkBasedKendallManager.loadDataset(filePath, column1, column2);
        endTime = System.currentTimeMillis();
        elapsedTimeSeconds = (endTime - startTime) / 1000.0;
        System.out.println("Spark InitialSetup and Dataset loading took: " + elapsedTimeSeconds + "\n");

        startTime = System.currentTimeMillis();
        double sparkTileKendallResult = sparkBasedKendallManager.calculateKendallTau(column1, column2);
        endTime = System.currentTimeMillis();
        elapsedTimeSeconds = (endTime - startTime) / 1000.0;
        printResults("Spark: Simple Structure + Stored Tiles", filePath, sparkTileKendallResult, elapsedTimeSeconds);
        
        Thread deleteThread = new Thread(() -> {
        	boolean deletionFlag = sparkBasedKendallManager.deleteSubFolders(new File(WriterSetup.getOutputExecDir()));
            System.err.println("Cleanup of tiles at " + WriterSetup.getOutputExecDir() + " was " + deletionFlag);
        });
        deleteThread.start();
    }


    private static void printResults(String methodName, String filePath, double kendallResult, double elapsedTimeSeconds) {
		// Print the result
    	System.out.println("\n\n" + " ----- ");
        System.out.println(methodName + " method for file " + filePath);
        System.out.println(methodName + " Kendall tau value:\t" + kendallResult);
        System.out.println(methodName+" elapsed time (sec):\t" + elapsedTimeSeconds + " seconds");
        System.out.println(" ----- \n");
	}
}