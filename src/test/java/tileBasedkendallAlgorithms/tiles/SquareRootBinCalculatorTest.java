package tileBasedkendallAlgorithms.tiles;

import org.apache.log4j.PropertyConfigurator;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import tileBasedKendallAlgorithms.algo.SquareRootBinCalculator;

import java.util.*;

import static org.junit.Assert.assertEquals;

public class SquareRootBinCalculatorTest {

    private SparkSession spark;

    @Before
    public void setUp() {
        PropertyConfigurator.configure("src/test/resources/input/log4j.properties");
        spark = SparkSession.builder()
                .appName("SquareRootBinCalculatorTest")
                .master("local[*]")
                .getOrCreate();
    }

    @Test
    public void testCalculateBin() {
        Dataset<Row> testDataset = createTestDataset();
        SquareRootBinCalculator binCalculator = new SquareRootBinCalculator();

        int numBins = binCalculator.calculateNumberOfBins(testDataset, "value");

        assertEquals(3, numBins);
    }

    @Test
    public void testCalculateBinWithEmptyDataset() {
        Dataset<Row> emptyDataset = spark.createDataFrame(Collections.emptyList(), createTestDataset().schema());
        SquareRootBinCalculator binCalculator = new SquareRootBinCalculator();

        int numBins = binCalculator.calculateNumberOfBins(emptyDataset, "value");

        assertEquals(0, numBins);
    }

    @Test
    public void testCalculateBinWithLargeDataset() {
        List<Row> largeData = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 10000; i++) {
            double randomValue = random.nextDouble();
            largeData.add(RowFactory.create(randomValue));
        }

        Dataset<Row> largeDataset = spark.createDataFrame(largeData, createTestDataset().schema());
        SquareRootBinCalculator binCalculator = new SquareRootBinCalculator();
        int numBins = binCalculator.calculateNumberOfBins(largeDataset, "value");
        assertEquals(100, numBins);
    }

    private Dataset<Row> createTestDataset() {
        StructType schema = DataTypes.createStructType(new StructField[]{
                DataTypes.createStructField("value", DataTypes.DoubleType, false)
        });

        List<Row> data = Arrays.asList(
                RowFactory.create(1.0),
                RowFactory.create(2.0),
                RowFactory.create(3.0),
                RowFactory.create(4.0),
                RowFactory.create(5.0),
                RowFactory.create(1.0),
                RowFactory.create(2.0)
        );

        // Create a Dataset from the sample data and schema
        return spark.createDataFrame(data, schema);
    }

    @After
    public void tearDown() {
        // Stop the Spark session after the tests
        spark.stop();
    }
}
