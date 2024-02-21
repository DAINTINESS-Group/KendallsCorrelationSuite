package client;

import listBasedKendallAlgorithms.ApacheCommonsKendall;
import listBasedKendallAlgorithms.ColumnPair;
import listBasedKendallAlgorithms.IListBasedKendallCalculator;
import listBasedKendallAlgorithms.ListBasedKendallMethodsService;
import listBasedKendallAlgorithms.Reader;
import tileBasedKendallAlgorithms.SparkBasedKendallManager;

import org.apache.spark.sql.AnalysisException;

import java.io.IOException;

public class DemoApp {
    public static void main(String[] args) throws IOException, AnalysisException {

        Reader reader = new Reader();
//        String filePath = "src\\test\\resources\\input\\TauAData.tsv";
//        String column1 = "X";
//        String column2 = "Y";
//        String delimiter = "\t";

//        String filePath = "src\\test\\resources\\input\\A_Data.csv";
//        String column1 = "low";
//        String column2 = "high";
//        String delimiter = ",";

//        String filePath = "src\\test\\resources\\input\\AAL_Data.csv";
//        String column1 = "low";
//        String column2 = "high";
//        String delimiter = ",";

//        String filePath = "src\\test\\resources\\input\\flights5_7m.csv"; // 5.7 million rows (Disclaimer: This takes 30 minutes (On my machine at least))
//        String column1 = "FLIGHT_NUMBER";
//        String column2 = "DEPARTURE_TIME";
//        String delimiter = ",";
//
//        String filePath = "src\\test\\resources\\input\\flights2_5m.csv"; // 2.5 million rows (Disclaimer: This takes 10 minutes (On my machine at least))
//        String column1 = "FLIGHT_NUMBER";
//        String column2 = "DEPARTURE_TIME";
//        String delimiter = ",";

        // manufacturer,model,year,price,transmission,mileage,fuelType,tax,mpg,engineSize
        String filePath = "src\\test\\resources\\input\\cars_100k.csv";
        String column1 = "mileage";
        String column2 = "mpg";
        String delimiter = ",";

//        String filePath = "src\\test\\resources\\input\\Random1Mil.csv";
//        String column1 = "X";
//        String column2 = "Y";
//        String delimiter = ",";

        ColumnPair columnPair = reader.read(filePath, column1, column2, delimiter);

        long startTime = -1;
        long endTime = -1;
        double elapsedTimeSeconds = -1.0;

        /* APACHE */
        startTime = System.currentTimeMillis();
        ApacheCommonsKendall apacheKendall = new ApacheCommonsKendall();
        double apacheResult = apacheKendall.calculateKendallTau(columnPair.getXColumn(), columnPair.getYColumn());
        endTime = System.currentTimeMillis();
        elapsedTimeSeconds = (endTime - startTime) / 1000.0;
        // Print the result
        System.out.println("Apache method for file " + filePath);
        System.out.println("Apache: " + apacheResult);
        System.out.println("Apache elapsed time: " + elapsedTimeSeconds + " seconds");
        System.out.println(" ----- \n");

        ListBasedKendallMethodsService methods = new ListBasedKendallMethodsService();

//        /* BRUTE */
//        IListBasedKendallCalculator bruteForceTauA = methods.getMethod("BruteForce");
//        startTime = System.currentTimeMillis();
//        double actualBruteForce = bruteForceTauA.calculateKendall(columnPair);
//        endTime = System.currentTimeMillis();
//        elapsedTimeSeconds = (endTime - startTime) / 1000.0;
//
//        // Print the result
//        System.out.println("Brute Force Test for file " + filePath);
//        System.out.println("Actual: " + bruteForceTauA);
//        System.out.println("brute force elapsed time: " + elapsedTimeSeconds + " seconds");
//        System.out.println(" ----- \n");
//
//        /* BROPHY */
//        IListBasedKendallCalculator brophyKendallTauB = methods.getMethod("Brophy");
//        startTime = System.currentTimeMillis();
//        double actualBrophy = brophyKendallTauB.calculateKendall(columnPair);
//        endTime = System.currentTimeMillis();
//        elapsedTimeSeconds = (endTime - startTime) / 1000.0;
//
//        System.out.println("Brophy Test for file " + filePath);
//        System.out.println("Actual: " + brophyKendallTauB);
//        System.out.println("Brophy elapsed time: " + elapsedTimeSeconds + " seconds");
//        System.out.println(" ----- \n");

        /* Tile Implementation with SPARK and valuePairs*/

        startTime = System.currentTimeMillis();
        SparkBasedKendallManager sparkBasedKendallManager = new SparkBasedKendallManager();
        sparkBasedKendallManager.loadDataset(filePath, column1, column2);
        endTime = System.currentTimeMillis();
        elapsedTimeSeconds = (endTime - startTime) / 1000.0;
        System.out.println("Spark InitialSetup and Dataset loading took: " + elapsedTimeSeconds + "\n");

        System.out.println(filePath);
        
        startTime = System.currentTimeMillis();
        double kendall = sparkBasedKendallManager.calculateKendallTau(column1, column2);
        endTime = System.currentTimeMillis();
        elapsedTimeSeconds = (endTime - startTime) / 1000.0;

        System.out.println("Tiles algorithm actual result: " + kendall);
        System.out.println("Tile valuePair elapsed time: " + elapsedTimeSeconds + " seconds");
        System.out.println(" ----- \n");
    }
}