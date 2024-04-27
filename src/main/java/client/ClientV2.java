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

//        /* TILES WITH LISTS*/
//        startTime = System.currentTimeMillis();
//        IListBasedKendallCalculator lbtbMgr = methods.createKendallCalculatorByString("ListBasedTiles");
//        double listTileKendallResult =lbtbMgr.calculateKendall(columnPair);
//        endTime = System.currentTimeMillis();
//        elapsedTimeSeconds = (endTime - startTime) / 1000.0; 
//        printResults("List Tiles", filePath, listTileKendallResult, elapsedTimeSeconds);
        
        /* TILES WITH MEMORY*/
        startTime = System.currentTimeMillis();
        IListBasedKendallCalculator bwmMgr = methods.createKendallCalculatorByString("BandsWithMemory");
        double bandsWithMemoryKendallResult =bwmMgr.calculateKendall(columnPair);
        endTime = System.currentTimeMillis();
        elapsedTimeSeconds = (endTime - startTime) / 1000.0; 
        printResults("Bands With Memory", filePath, bandsWithMemoryKendallResult, elapsedTimeSeconds);
        
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