package sparkBasedKendallAlgorithms;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SparkBasedKendallManagerSimpleTest {

    private final SparkBasedKendallManagerSimple sparkBasedKendallManagerSimple = new SparkBasedKendallManagerSimple();

    @Test
    public void testRegisterDatasetAndCalculateKendall() throws Exception {
        String path = "src/test/resources/testInput/TauAData.tsv";
        String column1 = "X";
        String column2 = "Y";

        sparkBasedKendallManagerSimple.loadDataset(path, column1, column2);
        double actual = sparkBasedKendallManagerSimple.calculateKendallTau(column1, column2);
        double expected = 0.23076923076923078;
        double delta = 0.0;
		System.out.println("\nSpark-Based Kendall (Tau B)");
		System.out.println("Expected:\t" + expected);
		System.out.println("Actual  :\t" + actual);
        assertEquals(expected, actual, delta);
    }
}
