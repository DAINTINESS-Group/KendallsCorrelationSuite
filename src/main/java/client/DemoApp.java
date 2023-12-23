package client;

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
        String filePath = "src/test/resources/input/TauBData.tsv";
        ColumnPair columnPair = reader.read(filePath, "X", "Y", "\t");

        KendallMethodsService methods = new KendallMethodsService();
        IKendallCalculator kendallCalculator = methods.getMethod("BruteForce");

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
                String.format("src%stest%sresources%sinput%sTauBData.tsv",
                        File.separator, File.separator, File.separator, File.separator));

        double kendall = datasetManager.calculateKendall("X", "Y", BinCalculatorMethods.SQUARE_ROOT);
        System.out.println("Result: " + kendall);

    }
}