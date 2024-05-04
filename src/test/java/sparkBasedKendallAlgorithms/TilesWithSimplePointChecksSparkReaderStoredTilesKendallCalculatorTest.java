package sparkBasedKendallAlgorithms;

import org.apache.spark.sql.AnalysisException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import util.writer.WriterSetup;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

@SuppressWarnings("unused")
@RunWith(Parameterized.class)
public class TilesWithSimplePointChecksSparkReaderStoredTilesKendallCalculatorTest extends SparkSessionTestSetup {

    private final String path;
    private final String column1;
    private final String column2;
	private final String delimiter;
    private final double expected;
    private final TilesWithSimplePointChecksSparkReaderStoredTilesKendallCalculator tilesWithSimplePointChecksSparkReaderStoredTilesKendallCalculator = new TilesWithSimplePointChecksSparkReaderStoredTilesKendallCalculator();

    public TilesWithSimplePointChecksSparkReaderStoredTilesKendallCalculatorTest(
            String path, String column1, String column2, String delimiter, double expected) {
        this.path = path;
        this.column1 = column1;
        this.column2 = column2;
        this.delimiter = delimiter;
        this.expected = expected;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"src\\test\\resources\\testInput\\TestFile.tsv", "X", "Y", "\t", 0.04957330142836763},
                {"src\\test\\resources\\testInput\\cars_10kTest.csv", "mpg", "mileage", ",", 0.2943112515766309},
                {"src\\test\\resources\\input\\cars_100k.csv", "mpg", "mileage", ",", 0.23002983829926982},
        });
    }

   
    @Test
    public void testCalculateKendall() {
        
        try {
			tilesWithSimplePointChecksSparkReaderStoredTilesKendallCalculator.loadDataset(path, column1, column2);
		} catch (AnalysisException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
        WriterSetup.OUTPUT_CUR_DIR = "try" + timeStamp + File.separator;
        System.err.println("Writing tiles at " + WriterSetup.getOutputExecDir());
        double actual = tilesWithSimplePointChecksSparkReaderStoredTilesKendallCalculator.calculateKendallTau(column1, column2);
        boolean deletionFlag = tilesWithSimplePointChecksSparkReaderStoredTilesKendallCalculator.deleteSubFolders(new File(WriterSetup.getOutputExecDir()));
        System.err.println("Cleanup of tiles at " + WriterSetup.getOutputExecDir() + " was " + deletionFlag);
        // Assert
        double delta = 0.0;
		System.out.println("\nHard-Disk Tiles Kendall (Tau B)");
		System.out.println("Expected:\t" + expected);
		System.out.println("Actual  :\t" + actual);
        assertEquals(expected, actual, delta);
    }
    
}
