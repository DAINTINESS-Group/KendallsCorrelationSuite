package client;

import tileBasedKendallAlgorithms.SparkBasedKendallManager;
import tileBasedKendallAlgorithms.algo.BinCalculatorFactory.BinCalculatorMethods;

import java.io.File;

import listBasedKendallAlgorithms.ApacheCommonsKendall;
import listBasedKendallAlgorithms.ColumnPair;
import listBasedKendallAlgorithms.IListBasedKendallCalculator;
import listBasedKendallAlgorithms.ListBasedKendallMethodsService;
import listBasedKendallAlgorithms.Reader;

import org.apache.spark.sql.AnalysisException;

import java.io.IOException;

public class DemoApp {
    public static void main(String[] args) throws IOException, AnalysisException {

        Reader reader = new Reader();
        String filePath = "src/test/resources/input/TauAData.tsv";
        ColumnPair columnPair = reader.read(filePath, "X", "Y", "\t");

        ListBasedKendallMethodsService methods = new ListBasedKendallMethodsService();
        IListBasedKendallCalculator listBasedKendallCalculator = methods.getMethod("BruteForce");

        ApacheCommonsKendall apacheKendall = new ApacheCommonsKendall();
        double apacheResult = apacheKendall.calculateKendallTau(columnPair.getXColumn(), columnPair.getYColumn());
        System.out.println("Apache: " + apacheResult);

        /* BRUTE */
        long startTime = System.nanoTime();

        double actual = listBasedKendallCalculator.calculateKendall(columnPair);
        System.out.println("Brute Force Test for file " + filePath);
        System.out.println("Actual: " + actual);
        System.out.println(" ----- \n");

        long endTime = System.nanoTime();
        double elapsedTimeSeconds = (endTime - startTime) / 1e9;
        // Print the result
        System.out.println("brute force elapsed time: " + elapsedTimeSeconds + " seconds");

        /* BROPHY */
        listBasedKendallCalculator = methods.getMethod("Brophy");
        startTime = System.nanoTime();

        actual = listBasedKendallCalculator.calculateKendall(columnPair);
        endTime = System.nanoTime();
        System.out.println("Brophy Test for file " + filePath);
        System.out.println("Actual: " + actual);
        System.out.println("Brophy elapsed time: " + elapsedTimeSeconds + " seconds");
        System.out.println(" ----- \n");

        elapsedTimeSeconds = (endTime - startTime) / 1e9;
        System.out.println("brute force elapsed time: " + elapsedTimeSeconds + " seconds");


        /* Tile Implementation with SPARK */
        SparkBasedKendallManager sparkBasedKendallManager = new SparkBasedKendallManager();

        sparkBasedKendallManager.loadDataset(
                String.format("src%stest%sresources%sinput%sTauAData.tsv",
                        File.separator, File.separator, File.separator, File.separator));

        double kendall = sparkBasedKendallManager.calculateKendallTau("X", "Y", BinCalculatorMethods.SQUARE_ROOT);

        System.out.println("Tiles algorithm result: " + kendall);
    }
}