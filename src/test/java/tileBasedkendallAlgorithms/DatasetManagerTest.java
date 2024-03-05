package tileBasedkendallAlgorithms;

import tileBasedKendallAlgorithms.SparkBasedKendallManager;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DatasetManagerTest {

    private final SparkBasedKendallManager sparkBasedKendallManager = new SparkBasedKendallManager();

    @Test
    public void testRegisterDatasetAndCalculateKendall() throws Exception {
        String path = "src/test/resources/testInput/TauAData.tsv";
        String column1 = "X";
        String column2 = "Y";

        sparkBasedKendallManager.loadDataset(path, column1, column2);
        double actual = sparkBasedKendallManager.calculateKendallTau(column1, column2);
        double expected = 0.23076923076923078;
        double delta = 0.0;

        assertEquals(expected, actual, delta);
    }
}
