package sparkBasedKendallAlgorithms.sparkSetup;

import org.apache.log4j.PropertyConfigurator;
import org.apache.spark.sql.SparkSession;

public class SparkSetup {

    static {
    	final String log4jConfigurationFile = "src/test/resources/input/log4j.properties";
    	final String correlatorHomeDir = "C:/hadoop";
    	
		System.setProperty("correlator.home.dir", correlatorHomeDir);
		PropertyConfigurator.configure(log4jConfigurationFile);
    }

    public SparkSession setup() {
        final String sparkSQLWarehouseDir = "file:/C:/tmp/spark-warehouse";
        
		return SparkSession.builder()
                .appName("KendallCorrelator")
                .master("local[*]")
                .config("spark.sql.warehouse.dir", sparkSQLWarehouseDir)
                .getOrCreate();
    }
}
