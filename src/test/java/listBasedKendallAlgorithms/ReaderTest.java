package listBasedKendallAlgorithms;

import org.junit.Test;
import org.junit.Assert;

import java.io.IOException;
import java.util.ArrayList;

/**
 * This class contains unit tests for the Reader class, which is responsible for reading CSV data.
 */
public class ReaderTest {

    Reader reader = new Reader();

    /**
     * Test case to check if an IOException is thrown when attempting to read an empty CSV file.
     *
     * @throws IOException if the file operation fails
     */
    @Test(expected = IOException.class)
    public void testEmptyFileIOException() throws IOException {
        String path = "src/test/resources/testInput/EmptyFileTest.csv";
        reader.read(path, "X", "Y", ",");
    }

    /**
     * Test case to check if an IllegalArgumentException is thrown when column names are not found in the CSV file.
     *
     * @throws IOException if the file operation fails
     */
    @Test(expected = IllegalArgumentException.class)
    public void testColumnNamesNotFoundException() throws IOException {
        String path = "src/test/resources/testInput/ColumnsNamesNotFoundTest.csv";
        reader.read(path, "X", "Y", ",");
    }

    /**
     * Test case to check if an IllegalArgumentException is thrown when there are not enough columns in the CSV file.
     *
     * @throws IOException if the file operation fails
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNotEnoughColumnsException() throws IOException {
        String path = "src/test/resources/testInput/NotEnoughColumnsTest.csv";
        reader.read(path, "X", "Y", ",");
    }

    /**
     * Test case to check if an IllegalArgumentException is thrown when there is a parsing error in the CSV data.
     *
     * @throws IOException if the file operation fails
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParsingErrorIllegalArgumentException() throws IOException {
        String path = "src/test/resources/testInput/ParsingErrorTest.csv";
        reader.read(path, "X", "Y", ",");
    }

    /**
     * Test case to validate a properly formatted CSV file with X and Y columns.
     * It ensures that all values in the X and Y columns are valid numbers.
     */
    @Test
    public void testValidCSV() {
        try {
            String path = "src/test/resources/input/TauBData.tsv";

            ColumnPair columnPair = reader.read(path, "X", "Y", "\t");
            Assert.assertNotNull(columnPair);

            ArrayList<Double> xValues = columnPair.getXColumn();
            ArrayList<Double> yValues = columnPair.getYColumn();

            assertAllValuesAreNumbers(xValues);
            assertAllValuesAreNumbers(yValues);

        } catch (IOException | IllegalArgumentException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Helper method to assert that all values in an ArrayList are valid numbers.
     *
     * @param values The ArrayList of values to check
     */
    private void assertAllValuesAreNumbers(ArrayList<Double> values) {
        for (Double value : values) {
            if (value == null || Double.isNaN(value) || Double.isInfinite(value)) {
                Assert.fail("Value is not a valid number: " + value);
            }
        }
    }
}
