package engine;

import tileBasedKendallAlgorithms.SparkBasedKendallManager;
import tileBasedKendallAlgorithms.algo.BinCalculatorFactory;

import org.apache.log4j.PropertyConfigurator;
import org.apache.spark.sql.SparkSession;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DatasetManagerTest {

    private final SparkBasedKendallManager sparkBasedKendallManager = new SparkBasedKendallManager();

    @Test
    public void testRegisterDatasetAndCalculateKendall() throws Exception {
        String path = "src/test/resources/input/TauAData.tsv";
        BinCalculatorFactory.BinCalculatorMethods binCalculationMethod = BinCalculatorFactory.BinCalculatorMethods.SQUARE_ROOT;

        sparkBasedKendallManager.registerDataset(path);
        double result = sparkBasedKendallManager.calculateKendall("X", "Y", binCalculationMethod);

        // TODO Update this test after implementing Tile Calculation
        assertEquals(0.0, result, 0.0);
    }
}
