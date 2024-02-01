package tileBasedkendallAlgorithms;

import org.apache.log4j.PropertyConfigurator;
import org.apache.spark.sql.SparkSession;
import org.junit.After;
import org.junit.Before;

public class SparkSessionTestSetup {

    protected SparkSession spark;

    @Before
    public void setUp() {
        PropertyConfigurator.configure("src/test/resources/input/log4j.properties");
        spark = SparkSession.builder()
                .appName("DatasetReaderTest")
                .master("local[*]")
                .getOrCreate();
    }

    @After
    public void tearDown() {
        spark.stop();
    }
}
