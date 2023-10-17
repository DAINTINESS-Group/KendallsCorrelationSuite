package client;

import kendall.IKendallCalculator;
import kendall.KendallMethodsService;
import reader.ColumnPair;
import reader.Reader;

import java.io.IOException;

public class DemoApp {
    public static void main(String[] args) throws IOException {

        Reader reader = new Reader();
        String filePath = "src/test/resources/input/TauAData.csv";
        ColumnPair columnPair = reader.read(filePath, "X", "Y", ";");
        
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
    }
}