package engine;

import org.apache.log4j.PropertyConfigurator;
import org.apache.spark.sql.SparkSession;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class SparkSetupTest {

    private SparkSetup sparkSetup;
    private SparkSession sparkSession;

    @Before
    public void setUp() {
        sparkSetup = new SparkSetup();
    }

    @After
    public void tearDown() {
        if (sparkSession != null) {
            sparkSession.stop();
        }
    }

    @Test
    public void testSetup() {

        sparkSession = sparkSetup.setup();

        assertNotNull(sparkSession);
        assertEquals("KendallCorrelator", sparkSession.conf().get("spark.app.name"));
        assertEquals("local[*]", sparkSession.conf().get("spark.master"));
        assertEquals("file:/C:/tmp/spark-warehouse", sparkSession.conf().get("spark.sql.warehouse.dir"));
    }
}
