package tileBasedKendallAlgorithms.sparkSetup;

import org.apache.log4j.PropertyConfigurator;
import org.apache.spark.sql.SparkSession;

public class SparkSetup {

    static {
        System.setProperty("correlator.home.dir", "C:/hadoop");
        PropertyConfigurator.configure("src/test/resources/input/log4j.properties");
    }

    public SparkSession setup() {
        return SparkSession.builder()
                .appName("KendallCorrelator")
                .master("local[*]")
                .config("spark.sql.warehouse.dir", "file:/C:/tmp/spark-warehouse")
                .getOrCreate();
    }
}
