package tileBasedkendallAlgorithms.reader;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.junit.Test;
import tileBasedKendallAlgorithms.reader.DatasetReader;
import tileBasedkendallAlgorithms.SparkSessionTestSetup;

import static org.junit.Assert.*;

public class DatasetReaderTest extends SparkSessionTestSetup {

    @Test
    public void testRead() {
        String path = "src/test/resources/testInput/ValidFileTest.tsv";
        String column1 = "X";
        String column2 = "Y";
        String delimiter = "\t";

        DatasetReader datasetReader = new DatasetReader(spark, path, delimiter);
        Dataset<Row> dataset = datasetReader.read(column1, column2);

        assertNotNull(dataset);
    }

    @Test
    public void testReadFileWithEmptyCell() {
        // Reading a file with 7 rows but a single row has an empty cell
        String path = "src/test/resources/testInput/EmptyCellTest.tsv";
        String column1 = "X";
        String column2 = "Y";
        String delimiter = "\t";

        DatasetReader datasetReader = new DatasetReader(spark, path, delimiter);
        Dataset<Row> dataset = datasetReader.read(column1, column2);

        // Input 7 rows. Expected to read 6
        assertEquals(dataset.count(), 6, 0);
    }
}

