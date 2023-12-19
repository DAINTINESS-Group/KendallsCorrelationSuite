package reader;

import org.apache.log4j.PropertyConfigurator;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DatasetReaderTest {

    private SparkSession spark;

    @Before
    public void setUp() {
        PropertyConfigurator.configure("src/test/resources/input/log4j.properties");
        spark = SparkSession.builder()
                .appName("DatasetReaderTest")
                .master("local[*]")
                .getOrCreate();
    }

    @Test
    public void testRead() {
        String path = "src/test/resources/testInput/ValidFileTest.tsv";
        String delimiter = "\t";

        DatasetReader datasetReader = new DatasetReader(spark, path, delimiter);
        Dataset<Row> result = datasetReader.read();

        assertNotNull(result);
    }

    @Test
    public void testReadFileWithEmptyCell() {
        // Reading a file with 7 rows but a single row has an empty cell
        String path = "src/test/resources/testInput/EmptyCellTest.tsv";
        String delimiter = "\t";

        DatasetReader datasetReader = new DatasetReader(spark, path, delimiter);
        Dataset<Row> result = datasetReader.read();

        // Received 7 rows. Expected to read 6
        assertEquals(result.count(), 6, 0);
    }

    @After
    public void tearDown() {
        // Stop the Spark session after the tests
        spark.stop();
    }
}

