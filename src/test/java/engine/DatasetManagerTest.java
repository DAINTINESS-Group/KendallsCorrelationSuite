package engine;

import kendall.BinCalculatorFactory;

import model.DatasetManager;
import org.apache.log4j.PropertyConfigurator;
import org.apache.spark.sql.SparkSession;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DatasetManagerTest {

    private final DatasetManager datasetManager = new DatasetManager();

    @Test
    public void testRegisterDatasetAndCalculateKendall() throws Exception {
        String path = "src/test/resources/input/TauAData.tsv";
        BinCalculatorFactory.BinCalculatorMethods binCalculationMethod = BinCalculatorFactory.BinCalculatorMethods.SQUARE_ROOT;

        datasetManager.registerDataset(path);
        double result = datasetManager.calculateKendall("X", "Y", binCalculationMethod);

        // TODO Update this test after implementing Tile Calculation
        assertEquals(0.0, result, 0.0);
    }
}
