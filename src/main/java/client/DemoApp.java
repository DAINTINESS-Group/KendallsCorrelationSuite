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
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import java.io.IOException;

public class DemoApp {
    public static void main(String[] args) throws IOException, AnalysisException {

        Reader reader = new Reader();
        String filePath = "src/test/resources/input/AAL_data.csv";
        ColumnPair columnPair = reader.read(filePath, "high", "low", ",");

        ListBasedKendallMethodsService methods = new ListBasedKendallMethodsService();
        IListBasedKendallCalculator listBasedKendallCalculator = methods.getMethod("BruteForce");

        ApacheCommonsKendall apacheKendall = new ApacheCommonsKendall();
        double apacheResult = apacheKendall.calculateKendallTau(columnPair.getXColumn(), columnPair.getYColumn());
        System.out.println("Apache: " + apacheResult);


        /* BRUTE */
        double actual = listBasedKendallCalculator.calculateKendall(columnPair);
        System.out.println("Brute Force Test for file " + filePath);
        System.out.println("Actual: " + actual);
        System.out.println(" ----- \n");

        /* BROPHY */
        listBasedKendallCalculator = methods.getMethod("Brophy");
        actual = listBasedKendallCalculator.calculateKendall(columnPair);
        System.out.println("Brophy Test for file " + filePath);
        System.out.println("Actual: " + actual);
        System.out.println(" ----- \n");

       /* Tile Implementation with SPARK */
        SparkBasedKendallManager sparkBasedKendallManager = new SparkBasedKendallManager();

        Dataset<Row> dataset = sparkBasedKendallManager.registerDataset(
                String.format("src%stest%sresources%sinput%sA_data.csv",
                        File.separator, File.separator, File.separator, File.separator));

        double kendall = sparkBasedKendallManager.calculateKendall("low", "high", BinCalculatorMethods.SQUARE_ROOT);

        System.out.println("Result: " + kendall);

    }
}