package tileBasedkendallAlgorithms.sparkSetup;

import org.apache.spark.sql.SparkSession;
import org.junit.Test;
import tileBasedKendallAlgorithms.sparkSetup.SparkSetup;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class SparkSetupTest {

    private final SparkSetup sparkSetup = new SparkSetup();

    @Test
    public void testSetup() {

        SparkSession sparkSession = sparkSetup.setup();
        assertNotNull(sparkSession);
        assertEquals("KendallCorrelator", sparkSession.conf().get("spark.app.name"));
        assertEquals("local[*]", sparkSession.conf().get("spark.master"));
        assertEquals("file:/C:/tmp/spark-warehouse", sparkSession.conf().get("spark.sql.warehouse.dir"));

        sparkSession.stop();
    }
}
