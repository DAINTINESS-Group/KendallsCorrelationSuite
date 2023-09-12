import model.ICalculator;
import model.KendallMethodsCollection;
import org.junit.Test;
import reader.ColumnPair;
import reader.Reader;

import static org.junit.Assert.*;

public class CalculatorTests {

    @Test
    public void testTauA() {
        Reader reader = new Reader();
        String filePath = "src/main/resources/TauAData.csv";

        ColumnPair columnPair = reader.read(filePath, "z", "x");
        KendallMethodsCollection methods = new KendallMethodsCollection();

        ICalculator calculator = methods.getMethod("BruteForce");
        ApacheCommonsKendall commons = new ApacheCommonsKendall();

        double actual = calculator.calculateKendall(columnPair);
        double expected = commons.calculateKendallTau(columnPair.getXColumn(), columnPair.getYColumn());

        System.out.println("Brute Force Test (Tau A)");
        System.out.println("Expected: " + expected);
        System.out.println("Actual: " + actual);

        assertEquals(expected, actual, 0.0);
    }

    @Test
    public void testBrophyMethod() {
        Reader reader = new Reader();
        String filePath = "src/main/resources/TauAData.csv";

        ColumnPair pair = reader.read(filePath, "z", "x");
        KendallMethodsCollection methods = new KendallMethodsCollection();

        ICalculator calculator = methods.getMethod("Brophy");
        ApacheCommonsKendall commons = new ApacheCommonsKendall();

        double actual = calculator.calculateKendall(pair);
        double expected = commons.calculateKendallTau(pair.getXColumn(), pair.getYColumn());

        System.out.println("\nBrophy Test (Tau B)");
        System.out.println("Expected: " + expected);
        System.out.println("Actual: " + actual);

        assertEquals(expected, actual, 0.0);
    }
}
