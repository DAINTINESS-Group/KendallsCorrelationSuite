package tileBasedkendallAlgorithms;

import tileBasedKendallAlgorithms.SparkBasedKendallManager;
import tileBasedKendallAlgorithms.algo.BinCalculatorFactory;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DatasetManagerTest {

    private final SparkBasedKendallManager sparkBasedKendallManager = new SparkBasedKendallManager();

    @Test
    public void testRegisterDatasetAndCalculateKendall() throws Exception {
        String path = "src/test/resources/input/TauAData.tsv";
        BinCalculatorFactory.BinCalculatorMethods binCalculationMethod = BinCalculatorFactory.BinCalculatorMethods.SQUARE_ROOT;

        sparkBasedKendallManager.loadDataset(path);
        double result = sparkBasedKendallManager.calculateKendallTau("X", "Y", binCalculationMethod);

        assertEquals(0.23076923076923078, result, 0.0);
    }
}
