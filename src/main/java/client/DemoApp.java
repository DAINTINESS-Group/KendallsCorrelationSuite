package client;

import kendall.ApacheCommonsKendall;
import model.DatasetManager;
import java.io.File;

import kendall.BinCalculatorFactory.BinCalculatorMethods;
import kendall.IKendallCalculator;
import kendall.KendallMethodsService;
import org.apache.spark.sql.AnalysisException;
import model.ColumnPair;
import reader.Reader;

import java.io.IOException;

public class DemoApp {
    public static void main(String[] args) throws IOException, AnalysisException {

        Reader reader = new Reader();
        String filePath = "src/test/resources/input/AAL_data.csv";
        ColumnPair columnPair = reader.read(filePath, "high", "low", ",");

        KendallMethodsService methods = new KendallMethodsService();
        IKendallCalculator kendallCalculator = methods.getMethod("BruteForce");

        ApacheCommonsKendall apacheKendall = new ApacheCommonsKendall();
        double apacheResult = apacheKendall.calculateKendallTau(columnPair.getXColumn(), columnPair.getYColumn());
        System.out.println("Apache: " + apacheResult);


        /* BRUTE */
        double actual = kendallCalculator.calculateKendall(columnPair);
        System.out.println("Brute Force Test for file " + filePath);
        System.out.println("Actual: " + actual);
        System.out.println(" ----- \n");

        /* BROPHY */
        kendallCalculator = methods.getMethod("Brophy");
        actual = kendallCalculator.calculateKendall(columnPair);
        System.out.println("Brophy Test for file " + filePath);
        System.out.println("Actual: " + actual);
        System.out.println(" ----- \n");

       /* Tile Implementation with SPARK */
        DatasetManager datasetManager = new DatasetManager();

        datasetManager.registerDataset(
                String.format("src%stest%sresources%sinput%sA_data.csv",
                        File.separator, File.separator, File.separator, File.separator));

        double kendall = datasetManager.calculateKendall("low", "high", BinCalculatorMethods.SQUARE_ROOT);
        System.out.println("Result: " + kendall);

    }
}