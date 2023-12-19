package engine;

import kendall.BinCalculatorFactory;

import org.apache.log4j.PropertyConfigurator;
import org.apache.spark.sql.SparkSession;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class DatasetManagerTest {

    private SparkSession spark;
    private DatasetManager datasetManager;

    @Before
    public void setUp() {
        PropertyConfigurator.configure("src/test/resources/input/log4j.properties");

        spark = SparkSession.builder()
                .appName("DatasetManagerTest")
                .master("local[2]")
                .getOrCreate();

        datasetManager = new DatasetManager();
        datasetManager.spark = spark;
    }

    @Test
    public void testRegisterDatasetAndCalculateKendall() throws Exception {
        String path = "src/test/resources/input/TauAData.tsv";
        BinCalculatorFactory.BinCalculatorMethods binCalculationMethod = BinCalculatorFactory.BinCalculatorMethods.SQUARE_ROOT;

        datasetManager.registerDataset(path);
        double result = datasetManager.calculateKendall("X", "Y", binCalculationMethod);

        // TODO Update this test after implementing Tile Calculation
        assertEquals(0.0, result, 0.0);
    }

    @After
    public void tearDown() {
        spark.stop();
    }
}
