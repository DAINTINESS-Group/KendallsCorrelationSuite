package client;

import java.io.File;

import listBasedKendallAlgorithms.ApacheCommonsKendall;
import listBasedKendallAlgorithms.ColumnPair;
import listBasedKendallAlgorithms.IListBasedKendallCalculator;
import listBasedKendallAlgorithms.ListBasedKendallMethodsService;
import listBasedKendallAlgorithms.Reader;
import newTryTiles.algo.BinCalculatorFactory.BinCalculatorMethods;
import newTryTiles.SparkBasedKendallManager;

import org.apache.spark.sql.AnalysisException;

import java.io.IOException;

public class DemoApp {
    public static void main(String[] args) throws IOException, AnalysisException {

        Reader reader = new Reader();
//        String filePath = "src\\test\\resources\\input\\TauAData.tsv"; 
//        String column1 = "X";
//        String column2 = "Y";
//        String delimiter = "\t";

        String filePath = "src\\test\\resources\\input\\A_Data.csv";
        String column1 = "low";
        String column2 = "high";
        String delimiter = ",";

//        String filePath = "src\\test\\resources\\input\\AAL_Data.csv";
//        String column1 = "low";
//        String column2 = "high";
//        String delimiter = ",";
        
//        //manufacturer,model,year,price,transmission,mileage,fuelType,tax,mpg,engineSize
//        String filePath = "src\\test\\resources\\input\\cars_100k.csv";
//        String column1 = "mpg";
//        String column2 = "mileage";
//        String delimiter = ",";
//        
        
        ColumnPair columnPair = reader.read(filePath, column1, column2, delimiter);
       
        ListBasedKendallMethodsService methods = new ListBasedKendallMethodsService();
        IListBasedKendallCalculator listBasedKendallCalculator = methods.getMethod("BruteForce");
        long startTime = -1;
        long endTime = -1;
        double elapsedTimeSeconds = -1.0;
        
        startTime = System.nanoTime();
        ApacheCommonsKendall apacheKendall = new ApacheCommonsKendall();
        double apacheResult = apacheKendall.calculateKendallTau(columnPair.getXColumn(), columnPair.getYColumn());
        endTime = System.nanoTime();
        elapsedTimeSeconds = (endTime - startTime) / 1e9;
        // Print the result
        System.out.println("Apache method for file " + filePath);
        System.out.println("Apache: " + apacheResult);
        System.out.println("Apache elapsed time: " + elapsedTimeSeconds + " seconds");
        System.out.println(" ----- \n");



        /* BRUTE */
        startTime = System.nanoTime();
        double actualBruteForce = listBasedKendallCalculator.calculateKendall(columnPair);
        endTime = System.nanoTime();
        elapsedTimeSeconds = (endTime - startTime) / 1e9;
        // Print the result
        System.out.println("Brute Force Test for file " + filePath);
        System.out.println("Actual: " + actualBruteForce);
        System.out.println("brute force elapsed time: " + elapsedTimeSeconds + " seconds");
        System.out.println(" ----- \n");
        
        /* BROPHY */
        listBasedKendallCalculator = methods.getMethod("Brophy");
        startTime = System.nanoTime();
        double actualBrophy = listBasedKendallCalculator.calculateKendall(columnPair);
        endTime = System.nanoTime();
        elapsedTimeSeconds = (endTime - startTime) / 1e9;

        System.out.println("Brophy Test for file " + filePath);
        System.out.println("Actual: " + actualBrophy);
        System.out.println("Brophy elapsed time: " + elapsedTimeSeconds + " seconds");
        System.out.println(" ----- \n");

        
        
        /* Tile Implementation with SPARK and valuePairs*/
        
        SparkBasedKendallManager sparkBasedKendallManager = new SparkBasedKendallManager();

        sparkBasedKendallManager.loadDataset(filePath);
   //             String.format("src%stest%sresources%sinput%sTauAData.tsv",
   //                     File.separator, File.separator, File.separator, File.separator));
        
        startTime = System.nanoTime();
        double kendall = sparkBasedKendallManager.calculateKendallTau(column1, column2, BinCalculatorMethods.SQUARE_ROOT);
        endTime = System.nanoTime();
        elapsedTimeSeconds = (endTime - startTime) / 1e9;

        System.out.println("Tiles algorithm actual result: " + kendall);
        System.out.println("Tile valuePair elapsed time: " + elapsedTimeSeconds + " seconds");
        System.out.println(" ----- \n");

    
    }
}