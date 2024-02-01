package tileBasedkendallAlgorithms.algo;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

import tileBasedKendallAlgorithms.algo.BinCalculatorFactory;
import tileBasedKendallAlgorithms.algo.IBinCalculator;
import tileBasedKendallAlgorithms.algo.TileBasedCalculatorService;
import tileBasedKendallAlgorithms.reader.DatasetReader;
import tileBasedkendallAlgorithms.SparkSessionTestSetup;

@RunWith(Parameterized.class)
public class TileBasedCalculatorServiceTest extends SparkSessionTestSetup {

    private final String path;
    private final String column1;
    private final String column2;
    private final String delimiter;
    private final double expected;

    public TileBasedCalculatorServiceTest(
            String path, String column1, String column2, String delimiter, double expected) {
        this.path = path;
        this.column1 = column1;
        this.column2 = column2;
        this.delimiter = delimiter;
        this.expected = expected;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"src\\test\\resources\\testInput\\TestFile.tsv", "X", "Y", "\t", 0.04957330142836763},
                {"src\\test\\resources\\testInput\\cars_10kTest.csv", "mpg", "mileage", ",", 0.2943112515766309},
                {"src\\test\\resources\\input\\cars_100k.csv", "mpg", "mileage", ",", 0.23002983829926982},
        });
    }

    @Test
    public void testCalculateKendallTauCorrelationWithDifferentDatasets() {
        // Arrange
        DatasetReader datasetReader = new DatasetReader(spark, path, delimiter);
        Dataset<Row> dataset = datasetReader.read();
        IBinCalculator binCalculator = new BinCalculatorFactory().createBinCalculator(BinCalculatorFactory.BinCalculatorMethods.SQUARE_ROOT);
        TileBasedCalculatorService service = new TileBasedCalculatorService(dataset, binCalculator, column1, column2);

        double actual = service.calculateKendallTauCorrelation();

        // Assert
        double delta = 0.0;
        assertEquals(expected, actual, delta);
    }
}
