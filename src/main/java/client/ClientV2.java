package client;

import listBasedKendallAlgorithms.*;
import listBasedKendallAlgorithms.listBasedReader.ColumnPair;
import listBasedKendallAlgorithms.listBasedReader.Reader;
import sparkBasedKendallAlgorithms.SparkBasedKendallManager;

import org.apache.spark.sql.AnalysisException;

import java.io.IOException;

public class ClientV2 {
    public static void main(String[] args) throws IOException, AnalysisException {

        Reader reader = new Reader();

        // manufacturer,model,year,price,transmission,mileage,fuelType,tax,mpg,engineSize
//        String filePath = "src\\test\\resources\\input\\cars_100k.csv";
//        String column1 = "mileage";
//        String column2 = "mpg";
//        String delimiter = ",";

//        String filePath = "src\\test\\resources\\input\\all_stocks_5yr.csv";
//        String column1 = "low";
//        String column2 = "high";
//        String delimiter = ",";

//        String filePath = "src\\test\\resources\\input\\all_stocks_5yr.csv";
//        String column1 = "close";
//        String column2 = "volume";
//        String delimiter = ",";

//        String filePath = "src\\test\\resources\\input\\flights5_7m.csv"; // 5.7 million rows
//        String column1 = "FLIGHT_NUMBER";
//        String column2 = "DEPARTURE_TIME";
//        String delimiter = ",";

//        String filePath = "src\\test\\resources\\input\\acs2017_census_tract_data.csv";
//        String column1 = "Hispanic";
//        String column2 = "Native";
//        String delimiter = ",";

        String filePath = "src\\test\\resources\\input\\acs2017_census_tract_data.csv";
        String column1 = "Men";
        String column2 = "Women";
        String delimiter = ",";

//        String filePath = "src\\test\\resources\\input\\Random1Mil.csv";
//        String column1 = "X";
//        String column2 = "Y";
//        String delimiter = ",";

        ColumnPair columnPair = reader.read(filePath, column1, column2, delimiter);

        long startTime = -1;
        long endTime = -1;
        double elapsedTimeSeconds = -1.0;

        ListBasedKendallFactory methods = new ListBasedKendallFactory();

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
        //ListBasedTileBasedKendallManager lbtbMgr = new ListBasedTileBasedKendallManager();
        double listTileKendallResult =lbtbMgr.calculateKendall(columnPair);
        endTime = System.currentTimeMillis();
        elapsedTimeSeconds = (endTime - startTime) / 1000.0;
        
        printResults("List Tiles", filePath, listTileKendallResult, elapsedTimeSeconds);
        
        /* BRUTE */
        IListBasedKendallCalculator bruteForceTauA = methods.createKendallCalculatorByString("BruteForce");
        startTime = System.currentTimeMillis();
        double actualBruteForce = bruteForceTauA.calculateKendall(columnPair);
        endTime = System.currentTimeMillis();
        elapsedTimeSeconds = (endTime - startTime) / 1000.0;

        // Print the result
        System.out.println("Brute Force Test for file " + filePath);
        System.out.println("Actual: " + actualBruteForce);
        System.out.println("brute force elapsed time: " + elapsedTimeSeconds + " seconds");
        System.out.println(" ----- \n");
//
//        /* BROPHY */
        IListBasedKendallCalculator brophyKendallTauB = methods.createKendallCalculatorByString("Brophy");
        startTime = System.currentTimeMillis();
        double actualBrophy = brophyKendallTauB.calculateKendall(columnPair);
        endTime = System.currentTimeMillis();
        elapsedTimeSeconds = (endTime - startTime) / 1000.0;

        System.out.println("Brophy Test for file " + filePath);
        System.out.println("Actual: " + actualBrophy);
        System.out.println("Brophy elapsed time: " + elapsedTimeSeconds + " seconds");
        System.out.println(" ----- \n");

        /* Tile Implementation with SPARK and valuePairs*/
        startTime = System.currentTimeMillis();
        SparkBasedKendallManager sparkBasedKendallManager = new SparkBasedKendallManager();
        sparkBasedKendallManager.loadDataset(filePath, column1, column2);
        endTime = System.currentTimeMillis();
        elapsedTimeSeconds = (endTime - startTime) / 1000.0;
        System.out.println("Spark InitialSetup and Dataset loading took: " + elapsedTimeSeconds + "\n");

        startTime = System.currentTimeMillis();
        double sparkKendall = sparkBasedKendallManager.calculateKendallTau(column1, column2);
        endTime = System.currentTimeMillis();
        elapsedTimeSeconds = (endTime - startTime) / 1000.0;

        printResults("Spark Kendall", filePath, sparkKendall, elapsedTimeSeconds);
    }


	private static void printResults(String methodName, String filePath, double kendallResult, double elapsedTimeSeconds) {
		// Print the result
        System.out.println("\n\n" + methodName + " method for file " + filePath);
        System.out.println(methodName + " kendallValue:\t" + kendallResult);
        System.out.println(methodName+" elapsed time:\t" + elapsedTimeSeconds + " seconds");
        System.out.println(" ----- \n");
	}
}